
var postPrivateLink = false;
var fullUrl = "";
var password = "";
var currentView = null;
var displayingError = false;

var create_qrcode = function (text, typeNumber, errorCorrectLevel, table) {
  var qr = qrcode(typeNumber || 4, errorCorrectLevel || 'M');
  qr.addData(text);
  qr.make();
  return qr.createImgTag();
};

function setView(view) {
    currentView.hide(100);
    view.show(100);
    currentView = view;
}

function showWaiting() {
    setView($("#waitingView"));
}

function showResult() {
    setView($("#resultView"));
}

function showSubmit() {
    setView($("#submitView"));
}

function displayError(errorText) {
    $("#errorHolder").show();
    $("#errorText").html(errorText);
    displayingError = true;
}

function postFullUrl() {
    $.post(
        "http://l.co.ua/generateShort",
        fullUrl,
        function (data) {
            if (data) {
                var url = "http://l.co.ua/" + data;
                $('#shortUrlText').val(url);
                $('#shortUrlText').focus().select();
                url = url.replace(/^[\s\u3000]+|[\s\u3000]+$/g, '');

                var qrHolder = $('#qrHolder');
                qrHolder.remove('current_qr');
                qrHolder.append(create_qrcode(url)).attr({"id": "current_qr", "class" : "center_image"});

                showResult();
            } else {
                showSubmit();
                displayError("Unknown error");
            }
        },
        'text'
    )
    .error(function(jqXHR, textStatus, errorThrown) {
        showSubmit();
        displayError(errorThrown);
    });

    showWaiting();
}

function postPrivateUrl() {
    alert("Private URL: " + fullUrl + "; psw: " + password);
    showWaiting();
}

document.addEventListener('DOMContentLoaded', function () {
    chrome.tabs.getSelected(null, function(tab) {
        fullUrl = tab.url;
    });

    var privateLinkCheckbox = $("#privateLinkCheckbox");
    var showPasswordCheckbox = $("#showPassword");
    var submitButton = $("#submitButton");
    var passwordText = $("#passwordText");

    privateLinkCheckbox.bind('change', function () {
        if ($("#privateLinkCheckbox").is(':checked')) {
            showPasswordBlock();
            postPrivateLink = true;
        } else {
            hidePasswordBlock();
            postPrivateLink = false;
        }
    });

    showPasswordCheckbox.click(function() {
        if (this.checked) {
            showPassword($("#passwordText"));
        } else {
            hidePassword($("#passwordText"));
        }
    });

    submitButton.click(function() {
        if (postPrivateLink) {
            postPrivateUrl();
        } else {
            postFullUrl();
        }

    });

    passwordText.change(function() {
        password = $(this).val();
    });

    currentView = $("#submitView");
});

function showPasswordBlock() {
    var passwordBox = $("#passwordBox");
    passwordBox.show(400);
}

function hidePasswordBlock() {
    var passwordBox = $("#passwordBox");
    passwordBox.hide(400);
}

function hidePassword(input) {
    setInputType(input, "password");
}

function showPassword(input) {
    setInputType(input, "text");
}

function setInputType(input, type) {
    var newEl = $("<input type='" + type + "' />");

    newEl.attr("id", input.attr("id"))
    newEl.attr("name", input.attr("name"))
    newEl.attr('class', input.attr('class'))
    newEl.val(input.val())
    newEl.insertBefore(input);

    input.remove();
}
