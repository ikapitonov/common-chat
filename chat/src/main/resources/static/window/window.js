window.scrollTo(0,document.body.scrollHeight);

var counter = document.querySelector("#users-counter");
var append  = document.querySelector(".chat-items");
var input   = document.querySelector("#text-input");

var user = document.querySelector("#username").innerHTML;
var site = document.querySelector("#sitename").innerHTML;
var tkey = document.querySelector("#token").innerHTML;

function counterAdd () {
    let num = parseInt(counter.innerHTML);

    counter.innerHTML = num + 1;

}

function counterRemove () {
    let num = parseInt(counter.innerHTML);

    counter.innerHTML = num - 1;
}

function addMessage (obj) {
    let elem = document.createElement("div");
    elem.classList.add("card", "mb-3", "message-card");

    let str = '<div class="card-header p-2 message-header">'
        + '<div class="message-header-wrap">'
        + '<div class="user_header">';

    str += obj.username;

    if (obj.admin && obj.admin > 0) {
        str += "<span class=\"badge ml-2 badge-warning\">Admin</span>";
    } else {
        str += "<span class=\"badge ml-2 badge-admin\">user</span>";
    }

    str  +=   "</div>"
        + '<div><small class="message-date">' + obj.date + '</small></div>'
        + '</div>'
        + '</div>'
        + '<div class="card-body text-dark p-2 message-text">'
        + obj.text
        + '</div>'
        + '</div>';

    elem.innerHTML = str;

    append.appendChild(elem);
    window.scrollTo(0,document.body.scrollHeight);
}

function sendMessage () {
    if (input.value && input.value != null && input.value != undefined && input.value != "" && input.value.trim() && input.value.trim() != "")
    {
        let message = {
            site: site,
            username: user,
            content: input.value.trim(),
            type: 'MESSAGE'
        };

        stompClient.send("/app/chat.sendMessage/" + site, {}, JSON.stringify(message));
    }
    input.value = null;
}

function connection () {
    let str = '/ws?site=' + site + "&username=" + user;

    if (tkey.length > 1) {
        str += "&token=" + tkey;
    }

    var socket = new SockJS(str);
    stompClient = Stomp.over(socket);
    stompClient.debug = null;
    stompClient.connect({}, onConnected, onError);
}

function onConnected () {
    stompClient.subscribe('/topic/sites/' + site, socketController);

    let message = {
        site: site,
        username: user,
        type: 'ADD'
    };

    stompClient.send("/app/chat.addUser/" + site,
        {},
        JSON.stringify(message)
    )
}

function socketController (payload) {
    let obj = JSON.parse(payload.body);

    if (obj.type == 'ADD') {
        counterAdd();
    }
    else if (obj.type == 'REMOVE') {
        counterRemove();
    }
    else if (obj.type == 'MESSAGE') {
        addMessage(obj);
    }
}

function onError () {
    stompClient.disconnect();
    document.querySelector(".container").innerHTML = "<h6 class='text-center mt-5 text-dark'>Cоединение с сервером прервано :(</h6>";
}

document.addEventListener("DOMContentLoaded", connection);
document.querySelector("#bnt-send").addEventListener("click", sendMessage);