var headers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

var rows = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

var gamePlayerId = getParameterByName('gp');

console.log(gamePlayerId);

var data;
var url = "/api/game_view/" + gamePlayerId;

var request = {
    method: 'GET',
    body: JSON.stringify(data),
};

fetch(url, request)
    .then(response => response.json())
    .then(data => {
        console.log(data);
        renderHeaders(headers);
        renderRows(rows);
        setGridColors(data);
        changeGamePlayerHeader(data);
    })
    .catch(error => {
        console.log(error);
    })

function changeGamePlayerHeader(data) {

    for (var i = 0; i < data.gamePlayers.length; i++) {
        var gamePlayerHeader = data.gamePlayers.player.email;
        console.log("test");

        if (data.gamePlayers.id === gamePlayerId) {
            document.getElementById("gamePlayerHeader").innerHTML = gamePlayerHeader + "(you";
        }
    }
}

function getHeadersHtml(headers) {
  return "<tr><th style='width: 50px'></th>" + headers.map(function(number) {
    return "<th style='width: 50px; text-align: center'>" + number + "</th>";
  }).join("") + "</tr>";
}

function renderHeaders(headers) {
  var html = getHeadersHtml(headers);
  document.getElementById("table-headers").innerHTML = html;
}

function getColumnsHtml(letter) {
  return headers.map(function(number) {
    return `<td style='width: 50px' id='${letter + number}'></td>`;
  }).join("")
}

function getShipLocations(data) {
    var ships = data.ships;
    var shipLocations = [];

    for (var i = 0; i < ships.length; i++) {
        var locations = ships[i].locations;
        locations.forEach((x) => shipLocations.push(x));
    }

   return shipLocations;
}

function setGridColors(data) {

    var shipLocations = getShipLocations(data);
    var table = document.getElementById('gameTable');
    var targetTDs = table.querySelectorAll('td');
    var tdIds = [];

    console.log(tdIds);
    console.log(shipLocations);

    for (var i = 0; i < targetTDs.length; i++) {
        var tdId = targetTDs[i].id;

        for (var j = 0; j < shipLocations.length; j++) {
            var shipLocation = shipLocations[j];

            if (tdId === shipLocation) {
                console.log("match");
                targetTDs[i].style.backgroundColor = "yellow";
            }
        }
    }
}

function getRowsHtml(rows) {
  return rows.map(function(letter) {
    return "<tr style='width: 50px; text-align: center'><th>" + letter + "</th>" +
      getColumnsHtml(letter) + "</tr>";
    }).join("");
}

function renderRows(rows) {
  var html = getRowsHtml(rows);
  document.getElementById("table-rows").innerHTML = html;
}