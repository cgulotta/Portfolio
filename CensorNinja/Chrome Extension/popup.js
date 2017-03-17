var onoffswitch = document.getElementById("myonoffswitch");
var countrySelector = document.getElementById("countryID");

onoffswitch.addEventListener("click", onOffFunction);

/* Send message to backend to get current value of select */

chrome.runtime.sendMessage({ from : "popup", subject : "selectValue"}, function(response) {
  // console.log(response.selectValue);
	// console.log(countrySelector.options[response.selectValue].value);

	if (response.selectValue) {
		countrySelector.value = countrySelector.options[response.selectValue].value;
  }
});

chrome.runtime.sendMessage({ from : "popup", subject : "buttonState"}, function(response) {
  // console.log(response.selectValue);
	// console.log(countrySelector.options[response.selectValue].value);
	// console.log(response);
	if (response.buttonState) {
		onoffswitch.value = response.buttonState;
  }
});

function onOffFunction() {
  console.log(onoffswitch);
	chrome.runtime.sendMessage({ from : "popup", subject : "buttonChanged", state : onoffswitch.checked }, function(response) {
		/* Not used */
	});
	if(onoffswitch.checked){
		chrome.tabs.getSelected(null, function(tab) {
			  // Send a request to the content script.
			  chrome.tabs.sendRequest(tab.id, {action: "censor"}, function(response) {
				// censors the page
			  });
			});
	} else {
		chrome.tabs.getSelected(null, function(tab) {
			  // Send a request to the content script.
			  chrome.tabs.sendRequest(tab.id, {action: "refresh"}, function(response) {
				//refreshes the page
			  });
			});
	}
}


countrySelector.addEventListener("change", function() {

	chrome.runtime.sendMessage({
	  from:    'content',
	  subject: 'selectChange',
		country : countrySelector.selectedIndex
	});

	chrome.tabs.getSelected(null, function(tab) {
			// Send a request to the content script.
			chrome.tabs.sendRequest(tab.id, { action: "country", country : countrySelector.options[countrySelector.selectedIndex].value }, function(response) {
			// censors the page
			});

			chrome.tabs.sendRequest(tab.id, { action: "updateCountry", country : countrySelector.selectedIndex }, function(response) {
			// censors the page
			});
		});
});
