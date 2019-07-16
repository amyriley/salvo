var app = new Vue({
    el: '#app',
    data: {
        headers: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
        rows: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
        gamePlayerId: "",
    },
    methods: {
        fetchData: function() {
            var data;
            var url = "/api/game_view/" + this.gamePlayerId;

            var request = {
                method: 'GET',
                body: JSON.stringify(data),
            };

            fetch(url, request)
                .then(response => response.json())
                .then(data => {
                    console.log(data);
                    this.setShipPositions(data);
                    this.setSalvoesFiredByGamePlayerId(data);
                    this.getSalvoesFiredByOpponent(data);
                    this.setHitPositions(data);
                    this.changeGamePlayerHeader(data);
                })
                .catch(error => {
                    console.log(error);
                })
        },
        getParameterByName: function(name, url) {
            if (!url) url = window.location.href;
            name = name.replace(/[\[\]]/g, '\\$&');
            var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
                results = regex.exec(url);
            if (!results) return null;
            if (!results[2]) return '';
            return decodeURIComponent(results[2].replace(/\+/g, ' '));
        },
        getShipLocations: function(data) {
            var ships = data.ships;
            var shipLocations = [];
        
            for (var i = 0; i < ships.length; i++) {
                var locations = ships[i].locations;
                locations.forEach((x) => shipLocations.push(x));
            }

           return shipLocations;
        },
        setShipPositions: function(data) {
            var shipLocations = this.getShipLocations(data);
            var table = document.getElementById('gameTable');
            var targetTDs = table.querySelectorAll('td');
        
            for (var i = 0; i < targetTDs.length; i++) {
                var tdId = targetTDs[i].id;
        
                for (var j = 0; j < shipLocations.length; j++) {
                    var shipLocation = shipLocations[j];
        
                    if (tdId === shipLocation) {
                        targetTDs[i].style.backgroundColor = "yellow";
                    }
                }
            }
        },
        getSalvoesFiredByGamePlayerId: function(data) {
            var gamePlayers = data.gamePlayers;
            var salvoesList = [];

            for (var i = 0; i < gamePlayers.length; i++) {

                if (gamePlayers[i].id == this.gamePlayerId) {
                    var salvoes = gamePlayers[i].salvoes;
                    salvoes.forEach((x) => salvoesList.push(x));
                }
            }
            
            return salvoes;
        },
        setSalvoesFiredByGamePlayerId: function(data) {
            var salvoes = this.getSalvoesFiredByGamePlayerId(data);
            var table = document.getElementById('salvoTable');
            var targetTDs = table.querySelectorAll('td');
        
            for (var i = 0; i < targetTDs.length; i++) {
                var tdId = targetTDs[i].id;
        
                for (var j = 0; j < salvoes.length; j++) {
                    var salvoLocations = salvoes[j].locations;
                    var salvoTurn = salvoes[j].turn;

                    for (var k = 0; k < salvoLocations.length; k++) {
                        if (tdId === salvoLocations[k]) {
                            targetTDs[i].style.backgroundColor = "green";
                            targetTDs[i].innerHTML = salvoTurn;
                        }
                    }
                }
            }
        },
        getSalvoesFiredByOpponent: function(data) {
            var gamePlayers = data.gamePlayers;
            var salvoesFiredByOpponent = [];

            for (var i = 0; i < gamePlayers.length; i++) {

                if (gamePlayers[i].id != this.gamePlayerId) {
                    var salvoes = gamePlayers[i].salvoes;
                    salvoes.forEach((x) => salvoesFiredByOpponent.push(x));
                }
            }
            
            return salvoesFiredByOpponent;
        },
        getOpponentHits: function(data) {
            var opponentSalvoes = this.getSalvoesFiredByOpponent(data);
            var shipLocations = this.getShipLocations(data);
            var hits = [];

            for (var i = 0; i < shipLocations.length; i++) {
                var ship = shipLocations[i];

                for (var j = 0; j < opponentSalvoes.length; j++) {
                    var salvoLocations = opponentSalvoes[j].locations;
                    var salvoTurn = opponentSalvoes[j].turn;

                    for (var k = 0; k < salvoLocations.length; k++) {
                        if (ship === salvoLocations[k]) {
                            hits.push({locations: salvoLocations[k], turn: salvoTurn});
                        }
                    }
                } 
            }

            return hits;
        },
        setHitPositions: function(data) {
            var hitsToShow = this.getOpponentHits(data);
            var table = document.getElementById('gameTable');
            var targetTDs = table.querySelectorAll('td');
        
            for (var i = 0; i < targetTDs.length; i++) {
                var tdId = targetTDs[i].id;
        
                for (var j = 0; j < hitsToShow.length; j++) {
                    var salvoLocations = hitsToShow[j].locations;
                    var salvoTurn = hitsToShow[j].turn;

                    if (tdId === salvoLocations) {
                        targetTDs[i].style.backgroundColor = "red";
                        targetTDs[i].innerHTML = salvoTurn;
                    }
                }
            }
        },
        changeGamePlayerHeader: function(data) {
            var emails = [];
            var gamePlayerHeader = document.getElementById("gamePlayerHeader");

            for (var i = 0; i < data.gamePlayers.length; i++) {
                var email = data.gamePlayers[i].player.email;
                var id = data.gamePlayers[i].id;

                if (id == this.gamePlayerId) {
                    emails.push(email + " (you)")
                } else {
                    emails.push(" " + email + " ");
                }
            }

            gamePlayerHeader.innerHTML = emails;
        },
    },
    created: function () {
        this.gamePlayerId = this.getParameterByName("gp");
        this.fetchData();
    },
});