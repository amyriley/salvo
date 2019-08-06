var app = new Vue({
    el: '#app',
    data: {
        headers: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
        rows: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
        gamePlayerId: "",
        leaderboard: ["Name", "Total", "Won", "Lost", "Tied"],
        uniquePlayers: [],
        results: [],
        scores: [],
        players: [],
        username: "",
        password: "",
        authenticated: false,
        games: [],
        gamesList: [],
        loggedInPlayersGames: [],
        gamePlayerIds: [],
        gamePlayersList: [],
        id: null
    },
    methods: {
        fetchData: function() {

            this.gamePlayerId = this.getParameterByName("gp");

            if (document.title === "Ship Locations!") {
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
            }
        },
        fetchGames: function() {
            var games;
            var url = "/api/games";

            var request = {
                method: 'GET',
                body: JSON.stringify(games),
            };

            fetch(url, request)
                .then(response => response.json())
                .then(games => {
                    console.log(games.games)
                    console.log("games " + games)
                    this.getGamePlayers
                    this.games = games.games;
                    this.getPlayers(games.games);
                    this.getGamesList(games.games);
                    this.shipLocations(7)
                })
                .catch(error => {
                    console.log(error);
                })
        },
        postGame: function() {
            fetch("/api/games", {
                method: 'POST',
                credentials: 'include',
                headers: {
                  'Accept': 'application/json',
                  'Content-type': 'application/x-www-form-urlencoded'
                },
                body: `username=${ this.username }`
              })
              .then(response => {
                console.log(response)
                if (response.status == 201) {
                  console.log("game created")
                return response.json();
                } else {
                  alert("Game not created");
                }
              }).then(function(data) {
                  window.location.href = "http://localhost:8080/web/game.html?gp=" + data.id;
              })
              .catch(error => console.log(error))
        },
        joinGame: function(gameId) {
            fetch("/api/game/" + gameId + "/players", {
                method: 'POST',
                credentials: 'include',
                headers: {
                  'Accept': 'application/json',
                  'Content-type': 'application/x-www-form-urlencoded'
                },
                body: `username=${ this.username }`
              })
              .then(response => {
                console.log(response)
                if (response.status == 201) {
                  console.log("you can join!")
                return response.json();
                } else {
                  alert("You cannot join this game");
                }
              }).then(function(data) {
                  window.location.href = "http://localhost:8080/web/game.html?gp=" + data.id;
              })
              .catch(error => console.log(error))
        },
        shipLocations: function(gamePlayerId) {
            fetch("/api/games/players/" + 7 + "/ships", {
                method: 'POST',
                credentials: 'include',
                headers: {
                  'Accept': 'application/json;charset=UTF-8',
                  'Content-type': 'application/json;charset=UTF-8'
                },
                body: JSON.stringify([ { "type": "test1", "locations": ["A1", "B1", "C1"] },
                { "type": "test2", "locations": ["H5", "H6"] }
                ])
              })
              .then(response => {
                console.log(response)
                if (response.status == 201) {
                  console.log("success!")
                return response.json();
                } else {
                  alert("Error");
                }
              })
              .catch(error => console.log(error))
        },
        getGamesList: function() {
            var games = this.games;
            var gamesList = this.gamesList;

            for (var i = 0; i < games.length; i++) {
                var game = games[i];
                gamesList.push({id: game.id, created: game.created.toLocaleString(), 
                    players: this.getGamePlayers(game), 
                    gamePlayerIds: this.getGamePlayerEmails(game)});
            }

            console.log("gamesList " + this.gamesList);
            return gamesList;
        },
        gamePlayerIdsContains: function(n) {
            return this.gamePlayerIds.indexOf(n) > -1
        },
        getGamePlayers: function(game) {
            var gamePlayers = [];
            var players = [];
            var gamePlayerIds = [];
            var emails = [];

            for (var i = 0; i < game.gamePlayers.length; i++) {
                var gamePlayer = game.gamePlayers[i];
                gamePlayers.push(gamePlayer);
            }

            for (var i = 0; i < gamePlayers.length; i++) {
                var player = gamePlayers[i].player;
                players.push(player);
                gamePlayerIds.push(gamePlayers[i].id);
            }

            for (var i = 0; i < players.length; i++) {
                var email = players[i].email;
                emails.push(email);
            }

            return emails;
        },
        getGamePlayerEmails: function(game) {
            var gamePlayerIds = [];
            var gamePlayers = [];
            var players = [];
            var gamePlayerIds = [];

            for (var i = 0; i < game.gamePlayers.length; i++) {
                var gamePlayer = game.gamePlayers[i];
                gamePlayers.push(gamePlayer);
            }

            for (var i = 0; i < gamePlayers.length; i++) {
                var player = gamePlayers[i].player;
                players.push(player);
                gamePlayerIds.push({email: gamePlayers[i].player.email, id: gamePlayers[i].id});
            }

            this.gamePlayerIds = gamePlayerIds;
            return gamePlayerIds;
        },
        // getLoggedInPlayersGames: function(games) {
        //     var loggedInPlayersGames = [];
        //     var gamePlayers = [];

        //     for (var i = 0; i < games.length; i++) {
        //         var gamePlayer = games[i].gamePlayers;
        //         gamePlayer.forEach((gamePlayer) => gamePlayers.push({gamePlayer: gamePlayer, gameId: games[i].id}));
        //     }

        //     for (var i = 0; i < gamePlayers.length; i++) {
        //         if (gamePlayers[i].gamePlayer.player.email == this.username) {
        //             loggedInPlayersGames.push({player: gamePlayers[i].gamePlayer.player.email, gameId: gamePlayers[i].gameId, gamePlayerId: gamePlayers[i].gamePlayer.id})
        //             this.gamePlayerId = gamePlayers[i].gamePlayer.player.id;
        //         }
        //     }

        //     this.loggedInPlayersGames = loggedInPlayersGames;
        //     return loggedInPlayersGames;
        // },
        postLogin: function() {
            fetch("/api/login", {
                method: 'POST',
                credentials: 'include',
                headers: {
                  'Accept': 'application/json',
                  'Content-type': 'application/x-www-form-urlencoded'
                },
                body: `username=${ this.username }&password=${ this.password }`
              })
              .then(response => {
                console.log(response)
                if (response.status == 200) {
                  console.log("logged in!")
                  this.authenticated = true;
                  this.fetchGames();
                } else {
                  alert("Invalid email or password")
                }
              })
              .catch(error => console.log(error))
        },
        postLogout: function() {
            fetch("/api/logout", {
                method: 'POST',
                credentials: 'include',
                headers: {
                  'Accept': 'application/json',
                  'Content-type': 'application/x-www-form-urlencoded'
                },
                body: `username=${ this.username }&password=${ this.password }`
              })
              .then(response => {
                console.log(response)
                if (response.status == 200) {
                  console.log("logged out!")
                  this.authenticated = false;
                } else {
                  alert("Invalid email or password")
                }
              })
              .then(function() {
                window.location.href = "http://localhost:8080/web/games.html";
              })
              .catch(error => console.log(error))
        },
        postSignup: function() {
            fetch("/api/players", {
                method: 'POST',
                credentials: 'include',
                headers: {
                  'Accept': 'application/json',
                  'Content-type': 'application/x-www-form-urlencoded'
                },
                body: `username=${ this.username }&password=${ this.password }`
              })
              .then(response => {
                console.log(response)
                if (response.status == 201) {
                  console.log("signed up!")
                  this.postLogin();
                } else {
                  alert("Sign up unsuccessful")
                }
              })
              .catch(error => console.log(error))
        },
        getScores: function(games) {
            var scores = [];

            if (games != null) {
                for (var i = 0; i < games.length; i++) {
                    var score = games[i].scores;
                    score.forEach((score) => scores.push(score));
                }
            }

            this.scores = scores;

            return scores;
        },
        getPlayers: function(games) {
            var scores = this.getScores(games);
            var results = [];
            var players = [];

            for (var i = 0; i < scores.length; i++) {
                var result = scores[i];
                players.push({player: result.playerName, total: this.getTotals(result.playerName), 
                    wins: this.getWinCount(result.playerName), losses: this.getLoseCount(result.playerName),
                    ties: this.getTieCount(result.playerName)});
            }
           
            var uniquePlayers = this.removeDuplicates(players, "player");
            uniquePlayers.sort((a, b) => (a.total < b.total) ? 1 : -1)

            this.players = players;
            this.results = results;
            this.scores = scores;
            this.uniquePlayers = uniquePlayers;

            return uniquePlayers;
        },
        removeDuplicates: function(originalArray, prop) {
            var newArray = [];
            var lookupObject  = {};
        
            for(var i in originalArray) {
                lookupObject[originalArray[i][prop]] = originalArray[i];
            }
        
            for(i in lookupObject) {
                newArray.push(lookupObject[i]);
            }
                
            return newArray;
        },
        getTotals: function(name) {
            var playerTotal = 0;
            var players = [];

            for (var i = 0; i < this.scores.length; i++) {
                var result = this.scores[i];
                var playerName = result.playerName;
                players.push({player: playerName, result: result.result});
            }

            for (var i = 0; i < players.length; i++) {
                if (players[i].player === name) {
                    var score = players[i].result;
                    playerTotal += score;
                }
            }

            return playerTotal;
        },
        getWinCount: function(name) {
            var wins = 0;
            var players = [];

            for (var i = 0; i < this.scores.length; i++) {
                var result = this.scores[i];
                var playerName = result.playerName;
                players.push({player: playerName, result: result.result});
            }

            for (var i = 0; i < players.length; i++) {
                if (players[i].player === name && players[i].result === 1) {
                    wins += 1;
                }
            }

            return wins;
        },
        getLoseCount: function(name) {
            var losses = 0;
            var players = [];

            for (var i = 0; i < this.scores.length; i++) {
                var result = this.scores[i];
                var playerName = result.playerName;
                players.push({player: playerName, result: result.result});
            }

            for (var i = 0; i < players.length; i++) {
                if (players[i].player === name && players[i].result === 0) {
                    losses += 1;
                }
            }

            return losses;
        },
        getTieCount: function(name) {
            var ties = 0;
            var players = [];

            for (var i = 0; i < this.scores.length; i++) {
                var result = this.scores[i];
                var playerName = result.playerName;
                players.push({player: playerName, result: result.result});
            }

            for (var i = 0; i < players.length; i++) {
                if (players[i].player === name && players[i].result === 0.5) {
                    ties += 1;
                }
            }

            return ties;
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
        this.fetchData();
        // this.gpId = document.getElementById("gamePlayer.id");
        // console.log("gpId " + this.gpId);
    },
});