// content.js
chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
    if( request.message === "clicked_browser_action" ) {
      var root_site_name = window.location.hostname;
      var full_site_name = window.location.toString();
      var article_name = document.title;
      chrome.runtime.sendMessage({"message": "parse", "url": full_site_name})
    }
  }
);
