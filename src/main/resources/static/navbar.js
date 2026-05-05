document.addEventListener("DOMContentLoaded", function () {
    fetch("/navbar.html")
        .then(function (response) {
            if (!response.ok) {
                throw new Error("navbar.html 載入失敗");
            }
            return response.text();
        })
        .then(function (html) {
            document.getElementById("navbar-container").innerHTML = html;
        })
        .catch(function (error) {
            console.error("Navbar 載入失敗：", error);
        });
});