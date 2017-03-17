
// Send a message to the active tab
chrome.tabs.query({active: true, currentWindow: true}, function(tabs) {
  var activeTab = tabs[0];
  chrome.tabs.sendMessage(activeTab.id, {"message": "clicked_browser_action"});
});

function process_summary(){
  var json_ob = JSON.parse(this.responseText);
  var num_items = json_ob.totalItemsCount;
  var item_a = json_ob.items;
  var summary = "";
  var title = item_a[0].text;
  for (var i = 1; i < item_a.length; i++){
    summary = summary.concat(item_a[i].text," ");
  }
  document.getElementById("title").innerHTML = title;
  document.getElementById("summary").innerHTML = summary;
}

function request_summary(url){
  var api_url = "http://api.intellexer.com/summarize?apikey=10d3f70c-7154-4458-978f-54c0e78ec83c&conceptsRestriction=7&returnedTopicsCount=2&summaryRestriction=7&textStreamLength=1000&url=";
  var req = new XMLHttpRequest();
  req.onload = process_summary
  req.open("GET", api_url.concat(url), true);
  req.send();
}

chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
    if( request.message === "parse" ) {
      console.log("requestURL: ".concat(request.url));
      //request_text_extract(request.url);
      request_summary(request.url);
    }
  }
);
