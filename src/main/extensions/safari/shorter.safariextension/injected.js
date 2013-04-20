function handleMessage(msg) {
    if (msg.name === 'getselection') {
        var link = null;
        var currentElement = window.getSelection().focusNode;
        while (currentElement != null)
        {
            if (currentElement.nodeType == Node.ELEMENT_NODE && currentElement.nodeName.toLowerCase() == 'a')
            {
                link = currentElement;
                break;
            }
            currentElement = currentElement.parentNode;
        }
        if (link) {
            safari.self.tab.dispatchMessage('theselection', link.toString());
        }

    }
}

safari.self.addEventListener("message", handleMessage, false);