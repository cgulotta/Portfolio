window.onload = function(){
  console.log("document loaded");
}

/*******************************************
ISS LIVE FEED CODE
*******************************************/
var tag = document.createElement('script');

tag.src = "https://www.youtube.com/iframe_api";
var firstScriptTag = document.getElementsByTagName('script')[0];
firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

var player;
function onYouTubeIframeAPIReady() {
  player = new YT.Player('ISS_VID', {
    height: '500',
    width: '500',
    videoId: 'ddFvjfvPnqk',
    playerVars: {'autoplay': 1,'controls': 0,'showinfo': 0},
    events: {
      'onReady': onPlayerReady
    }
  });
}

function onPlayerReady(event) {
  event.target.mute();
  event.target.playVideo();
  document.getElementById('ISS_VID').style.zIndex = "-1";
  document.getElementById('ISS_VID').style.pointerEvents = "none";
}
