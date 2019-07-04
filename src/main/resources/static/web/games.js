var data;
var url = "/api/games"

var request = {
    method: 'GET',
    body: JSON.stringify(data),
};

fetch(url, request)
    .then(response => response.json())
    .then(data => {
        console.log(data);
        renderGames(data);
    })
    .catch(error => {
        console.log(error);
    })

 function getItemHtml(data) {
   return "<li class='list-group-item' style='width: 50em'><span>"
     + data.created.toLocaleString() + ": " + data.gamePlayers.map(gamePlayer => " " + gamePlayer.player.email) + "</span>" + "</li>";
 }

 function getListHtml(data) {
   return data.map(getItemHtml).join("");
 }

 function renderGames(data) {
   var html = getListHtml(data);
   document.getElementById("games").innerHTML = html;
 }