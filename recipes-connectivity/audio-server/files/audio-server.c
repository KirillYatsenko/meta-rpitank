/*
 * Copyright (C) 2016-2023 Davidson Francis <davidsondfgl@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

#define _POSIX_SOURCE

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <ws.h>
#include <errno.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <signal.h>

#define DISABLE_VERBOSE

/**
 * @dir examples/
 * @brief wsServer examples folder
 */

/*
 * @dir examples/echo
 * @brief Echo example directory.
 * @file echo.c
 * @brief Simple echo example.
 */

int stdin_pipe[2];
pid_t child_pid;
/**
 * @brief Called when a client connects to the server.
 *
 * @param client Client connection. The @p client parameter is used
 * in order to send messages and retrieve informations about the
 * client.
 */
void onopen(ws_cli_conn_t *client)
{
	char *cli, *port;
	cli  = ws_getaddress(client);
	port = ws_getport(client);
	char print_buffer[256];
	int result;

#ifndef DISABLE_VERBOSE
	printf("Connection opened, addr: %s, port: %s\n", cli, port);
#endif

  // First, create the pipe.
  result = pipe(stdin_pipe);
  if (result != 0) {
    printf("Failed creating communication pipe: %s\n", strerror(errno));
    return;
  }

  // Next, fork.
  child_pid = fork();
  if (child_pid < 0) {
    printf("Fork error: %s\n", strerror(errno));
    return;
  }

  if (child_pid == 0) {
    // Before exec'ing, the child must set the receiving end of the pipe to
    // stdin.
    result = dup2(stdin_pipe[0], STDIN_FILENO);
    if (result < 0) {
      printf("Failed setting pipe to stdin descriptor: %s\n", strerror(errno));
      return;
    }
    close(stdin_pipe[1]);
  //-fflags nobuffer -flags low_delay -framedrop -strict experimental
    result = execlp("ffplay", "ffplay", "-fflags", "nobuffer", "-flags", "low_delay", "-framedrop", "-strict", "experimental", "-nodisp", "-", (char *)NULL);
    if (result < 0) {
      printf("Failed exec'ing a process: %s\n", strerror(errno));
    }
  }
 }

/**
 * @brief Called when a client disconnects to the server.
 *
 * @param client Client connection. The @p client parameter is used
 * in order to send messages and retrieve informations about the
 * client.
 */
void onclose(ws_cli_conn_t *client)
{
	char *cli;
	cli = ws_getaddress(client);
#ifndef DISABLE_VERBOSE
	printf("Connection closed, addr: %s\n", cli);
#endif

	printf("Connection closed!\n");

	if (kill(child_pid, SIGTERM)) {
		printf("Unable to kill a ffplay process: %s, trying SIGKILL then\n", strerror(errno));
		if (kill(child_pid, SIGKILL)) {
			printf("SIGKILL doesn't help as well: %s\n", strerror(errno));
		}
	}
}

/**
 * 
 * @brief Called when a client connects to the server.
 *
 * @param client Client connection. The @p client parameter is used
 * in order to send messages and retrieve informations about the
 * client.
 *
 * @param msg Received message, this message can be a text
 * or binary message.
 *
 * @param size Message size (in bytes).
 *
 * @param type Message type.
 */
void onmessage(ws_cli_conn_t *client,
	const unsigned char *msg, uint64_t size, int type)
{
	char *cli;
	cli = ws_getaddress(client);
#ifndef DISABLE_VERBOSE
	printf("I receive a message: %s (size: %" PRId64 ", type: %d), from: %s\n",
		msg, size, type, cli);
#endif

	// fwrite(msg, 1, size, stdout);
	// fflush(stdout);
//  fsync(stdout);
//
 // Now, the parent can write data to its end of the pipe. The child `cat`
  // process should print out this string.

  close(stdin_pipe[0]);
  if (write(stdin_pipe[1], msg, size) <= 0)
	printf("Failed writing to pipe: %s\n", strerror(errno));
  else
	  printf("write success\n");

  // memset(print_buffer, 0, sizeof(print_buffer));
  // snprintf(print_buffer, sizeof(print_buffer), "Hello! %s %f\n", "whatever",
  //   -1337.12);
  // if (write(stdin_pipe[1], print_buffer, strlen(print_buffer) + 1) <= 0) {
  //   printf("Failed writing to pipe: %s\n", strerror(errno));
  //   return;
  // }

  // close(stdin_pipe[1]);


	/**
	 * Mimicks the same frame type received and re-send it again
	 *
	 * Please note that we could just use a ws_sendframe_txt()
	 * or ws_sendframe_bin() here, but we're just being safe
	 * and re-sending the very same frame type and content
	 * again.
	 *
	 * Alternative functions:
	 *   ws_sendframe()
	 *   ws_sendframe_txt()
	 *   ws_sendframe_txt_bcast()
	 *   ws_sendframe_bin()
	 *   ws_sendframe_bin_bcast()
	 */
	ws_sendframe_bcast(8080, (char *)msg, size, type);
}

/**
 * @brief Main routine.
 *
 * @note After invoking @ref ws_socket, this routine never returns,
 * unless if invoked from a different thread.
 */
int main(void)
{
	ws_socket(&(struct ws_server){
		/*
		 * Bind host:
		 * localhost -> localhost/127.0.0.1
		 * 0.0.0.0   -> global IPv4
		 * ::        -> global IPv4+IPv6 (DualStack)
		 */
		.host = "0.0.0.0",
		.port = 6000,
		.thread_loop   = 0,
		.timeout_ms    = 1000,
		.evs.onopen    = &onopen,
		.evs.onclose   = &onclose,
		.evs.onmessage = &onmessage
	});

	/*
	 * If you want to execute code past ws_socket(), set
	 * .thread_loop to '1'.
	 */

	return (0);
}
