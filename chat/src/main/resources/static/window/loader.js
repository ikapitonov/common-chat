var CanLoading = true;
var startLoading = 50;

function maxHeight () {
    var body = document.body,
        html = document.documentElement;

    return Math.max( body.scrollHeight, body.offsetHeight,
        html.clientHeight, html.scrollHeight, html.offsetHeight );
}

function loadingItems (data) {
    let num = 50 + startLoading;
    let str = "<div id='messageLoader" + num + "'></div>" + data;
    let height = maxHeight();

    document.querySelector("#messageLoader" + startLoading).innerHTML = str;

    window.scrollTo(0, maxHeight() - height);

    startLoading += 50;
    CanLoading = true;
}

function  Scrolling () {
    if (CanLoading === true && window.pageYOffset < 31) {
        CanLoading = false;

        $.ajax ({
            type: 'GET',
            url: 'loading',
            data: {
                items: startLoading,
                site: document.querySelector("#sitename").innerHTML
            },
            success: function (data) {
                if (!data || data == null || data == "" || data.length < 10) {
                    setTimeout(function () {
                        document.querySelector(".chLodader").innerHTML = "Сообщений: " + document.querySelectorAll(".message-card").length;
                    }, 400);
                }
                else {
                    loadingItems(data);
                }
            }
        });
    }
}


function CreateScrolling () {
    document.addEventListener("scroll", Scrolling);
}
document.addEventListener("DOMContentLoaded", CreateScrolling);