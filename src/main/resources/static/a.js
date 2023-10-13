function send(method, url) {
    let xhttp = new XMLHttpRequest();
    xhttp.onload = function () {
        console.log(this.responseText);
        //document.getElementById("demo").innerHTML =
        //this.responseText;
    }
    xhttp.onreadystatechange = function () {
        if (xhttp.readyState === 4) {
            console.log(xhttp)
            let out = document.querySelector('#out')
            let e;


            e = document.createElement('div')
            e.innerText = JSON.stringify(JSON.parse(xhttp.responseText), null, 3)
            e.classList.add('json')
            out.insertBefore(e, out.firstChild);

            e = document.createElement('div')
            e.innerText = xhttp.status;
            e.classList.add('status')
            if (xhttp.status <= 200 && xhttp.status < 300) {
                e.classList.add('success')
            } else {
                e.classList.add('fail')
            }
            out.insertBefore(e, out.firstChild);

            e = document.createElement('div')
            e.innerText = method + ' ' + url;
            e.classList.add('url')
            out.insertBefore(e, out.firstChild);

            e = document.createElement('div')
            e.innerText = new Date().toLocaleTimeString();
            e.classList.add('date')
            out.insertBefore(e, out.firstChild);
        }

    };

    xhttp.open(method, url);
    xhttp.send();
}

document.querySelector('#start').onclick = () => {
    let count = document.querySelector('#count').value
    send('POST', '/searching/start/' + count);
}
document.querySelector('#stop').onclick = () => {
    send('DELETE', '/searching/stop');
}
document.querySelector('#get').onclick = () => {
    let from = document.querySelector('#from').value
    let to = document.querySelector('#to').value
    send('GET', '/prim/list/' + from + '/' + to);
}
