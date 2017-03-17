var currentIndex = undefined;
var buttonState = undefined;

chrome.runtime.onMessage.addListener(function (msg, sender, sendResponse) {
  // First, validate the message's structure
  // console.log(msg);

  if ((msg.from === 'popup')) {
    // Enable the page-action for the requesting tab
    //chrome.pageAction.show(sender.tab.id);
    if (msg.subject === 'selectChange') {
      /* Set the new index */
      currentIndex = msg.country;
    }

    if (msg.subject === 'selectValue') {
      /* Respond with a message */
      sendResponse({selectValue : currentIndex});
    }

    if (msg.subject === 'buttonChanged') {
      console.log('Button changed');
      console.log(msg.state);
      buttonState = msg.state;
    }

    if (msg.subject === 'buttonState') {
      /* Respond with a message */
      sendResponse({state : buttonState});
    }

  }

});

chrome.browserAction.setPopup({popup : 'popup.html'});
