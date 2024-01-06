async function stop() {
	await fetch("action?stop", { method: "POST" })
}

async function front() {
	await fetch("action?front", { method: "POST" })
}

async function left() {
	await fetch("action?left", { method: "POST" })
}

async function right() {
	await fetch("action?right", { method: "POST" })
}

async function back() {
	await fetch("action?back", { method: "POST" })
}

const sleep = time => new Promise(resolve => setTimeout(resolve, time));

var mediaRecorder;
var wsMedia;

async function stopMicStreaming() {
	if (mediaRecorder == null)
		return;

	mediaRecorder.stop();
	wsMedia.close();

	var stopBtn = document.getElementById("mic-stop");
	var startBtn = document.getElementById("mic-start");

	stopBtn.style.display = "none"
	startBtn.style.display = "inline-block"
}

async function startMicStreaming() {
	var startBtn = document.getElementById("mic-start");
	var stopBtn = document.getElementById("mic-stop");

	startBtn.style.display = "none"
	stopBtn.style.display = "inline-block"

	var stream = await navigator.mediaDevices.getUserMedia({ audio: true });
	mediaRecorder = new MediaRecorder(stream, {audioBitsPerSecond: 12000});

	var wsProtocol = "wss://";
	if (location.host == "robot.local")
		wsProtocol = "ws://"; // there is no secure WebSockets on localhost

	wsMedia = new WebSocket(wsProtocol + location.host + "/wsck/");

	wsMedia.onopen = function() {
		// send stream every 100ms chunks
		mediaRecorder.start(100);
	};

	mediaRecorder.addEventListener("dataavailable", async event => {
		if (wsMedia.readyState == 1) // connection is open
			wsMedia.send(event.data);
	});
}

function reloadVideo() {
	var image = document.getElementById("camera-img");
	src = "http://" + window.location.hostname + ":8081/0/stream";
	image.src = src;
}

async function configureVideo() {
	const response = await fetch("video?status", { method: "GET" })
	const videoStatus = await response.text();

	if (videoStatus == "running") {
		var startBtn = document.getElementById("video-start");
		var stopBtn = document.getElementById("video-stop");
		startBtn.style.display = "none"
		stopBtn.style.display = "inline-block"
		reloadVideo();
	}
}

async function startVideoStreaming() {
	var startBtn = document.getElementById("video-start");
	startBtn.disabled = true;

	await fetch("video?start", { method: "POST" })

	// give time for motion service to start before reloading
	await new Promise(r => setTimeout(r, 1000));

	location.reload();
}

async function stopVideoStreaming() {
	var stopBtn = document.getElementById("video-stop");
	stopBtn.disabled = true;

	await fetch("video?stop", { method: "POST" })

	// give time for motion service to stop before reloading
	await new Promise(r => setTimeout(r, 1000));

	location.reload();
}

window.addEventListener('keydown', function(event) {
	if (event.repeat) return;

	const key = event.key;
	switch (event.key) {
		 case "ArrowLeft":
			left()
			break;
		case "ArrowRight":
			right()
			break;
		case "ArrowUp":
			front()
			break;
		case "ArrowDown":
			back()
			break;
		}
});

window.addEventListener('keyup', function(event) {
	stop()
});

window.onload = async function() {
	await configureVideo();
}
