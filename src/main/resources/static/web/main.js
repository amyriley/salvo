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
        id: null,
        ships: [{number: 1, type: "Aircraft carrier", length: 5, positions: [], placing: false, placed: false}, 
        {number: 1, type: "Battleship", length: 4, positions: [], placing: false, placed: false}, 
        {number: 1, type: "Submarine", length: 3, positions: [], placing: false, placed: false}, 
        {number: 1, type: "Destroyer", length: 3, positions: [], placing: false, placed: false}, 
        {number: 1, type: "Patrol boat", length: 2, positions: [], placing: false, placed: false}],
        salvo: {locations: []},
        salvoes: [],
        salvoTable: "salvoTable",
        shipLength: null,
        shipType: null,
        location: null,
        endLocation: null,
        possibleShipPositions: [],
        placing: false,
        gameStats: [],
        turnToPlaceSalvoes: false,
        shipsPlaced: false,
        gameOver: false,  
        shipSelected: false,
        result: 0,
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
                        this.showShipPositions(data);
                        this.getShipHits(data);
                        this.getHitsOnOpponent(data);
                        this.showSalvoesFired(data);
                        this.showHitsOnOpponent(this.getHitsOnOpponent(data))
                        this.getSalvoesFiredByOpponent(data);
                        this.changeGamePlayerHeader(data);
                        this.turnToPlaceSalvoes = this.getTurnToPlaceSalvoes(data, this.gamePlayerId);
                        this.shipsPlaced = this.checkIfShipsPlaced(data);
                        this.gameOver = this.checkGameOverStatus(data);
                        if (this.checkGameOverStatus(data)) {
                            this.showResult(data);
                        }
                        if (!this.gameOver){
                            setTimeout(this.fetchData, 2000);
                        }
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
                    this.getGamePlayers;
                    this.games = games.games;
                    this.getPlayers(games.games);
                    this.getGamesList(games.games);
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
                  window.location.href = "/web/game.html?gp=" + data.id;
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
                  window.location.href = "/web/game.html?gp=" + data.id;
              })
              .catch(error => console.log(error))
        },
        sendShipLocations: function() {
            if (this.allShipsPlaced()) {
                fetch("/api/games/players/" + this.gamePlayerId + "/ships", {
                    method: 'POST',
                    credentials: 'include',
                    headers: {
                        'Accept': 'application/json;charset=UTF-8',
                        'Content-type': 'application/json;charset=UTF-8'
                    },
                    body: JSON.stringify(this.getShipLocations())
                    })
                    .then(response => {
                    console.log(response)
                    if (response.status == 201) {
                        console.log("Success!")
                    return response.json();
                    } else {
                        alert("Error");
                    }
                    })
                    .catch(error => console.log(error))
            } else {
                alert("You need to place all ships before confirming!")
            }
        },
        sendSalvoLocations: function() {
            this.salvoes.push(this.salvo);

            fetch("/api/games/players/" + this.gamePlayerId + "/salvos", {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Accept': 'application/json;charset=UTF-8',
                'Content-type': 'application/json;charset=UTF-8'
            },
            body: JSON.stringify(this.salvoes)
            })
            .then(response => {
            console.log(response)
            if (response.status == 201) {
                console.log("Success!")
                this.salvoes = [];
                this.salvo.locations = [];
            return response.json();
            } else {
                alert("Error");
            }
            })
            .catch(error => console.log(error))
        },
        checkGameOverStatus: function(data) {
            if (data.gameOver == false) {
                return false;
            }

            return true;
        }, 
        showResult: function(data) {
            var scores = data.scores;

            for (var i = 0; i < scores.length; i++) {
                this.result = scores[i].result;
            }

            return this.result;
        },
        getTurnToPlaceSalvoes: function(data, gamePlayerId) {
            var gamePlayers = data.gamePlayers;
            var turnToPlaceSalvoes;

            for (var i = 0; i < gamePlayers.length; i++) {
                if (gamePlayers[i].id == gamePlayerId) {
                    turnToPlaceSalvoes = gamePlayers[i].turnToPlaceSalvoes;
                }
            }

            return turnToPlaceSalvoes;
        },
        selectShip: function() {
            this.shipSelected = true;

            var shipLength = document.querySelector('input[name="ship_selector"]:checked').value;
            var shipType = document.querySelector('input[name="ship_selector"]:checked').id;
            var ships = this.ships;
            this.shipLength = shipLength;
            this.shipType = shipType;

            for (var i = 0; i < ships.length; i++) {
                if (shipType === ships[i].type && ships[i].placed == false) {
                    ships[i].placing = true;
                }
            }

            return shipLength;
        },
        allShipsPlaced: function() {
            var ships = this.ships;

            for (var i = 0; i < ships.length; i++) {
                if (ships[i].positions.length == 0) {
                    return false;
                }
            }

            return true;
        },
        checkIfShipsPlaced: function(data) {
            var ships = data.ships;

            if (ships.length == 5) {
                return true;
            }

            return false;
        },
        showStartLocation: function(location) {
            var table = document.getElementById("gameTable");
            var targetTDs = table.querySelectorAll('td');

            for (var i = 0; i < targetTDs.length; i++) {
                var tdId = targetTDs[i].id;

                if (tdId == location) {
                    targetTDs[i].style.backgroundColor = "green";
                }
            }

            for (var i = 0; i < this.ships.length; i++) {
                if (this.ships[i].placing == true) {
                    this.showPossibleShipLocations(location);
                }
            }

            return location;
        },
        showPossibleShipLocations: function(location) {
            var possiblePositions = this.calculateShipEndLocation(location);

            if (possiblePositions.length > 0) {
                var table = document.getElementById("gameTable");
                var targetTDs = table.querySelectorAll('td');
    
                for (var i = 0; i < targetTDs.length; i++) {
                    var tdId = targetTDs[i].id;
                    for (var j = 0; j < possiblePositions.length; j++) {
                        if (tdId == possiblePositions[j] && targetTDs[i].style.backgroundColor != "red") {
                            targetTDs[i].style.backgroundColor = "gray";
                        }
                    }
                }
                
                this.selectEndLocation(possiblePositions, location);
            }

            return possiblePositions;
        },
        clearPlacerMarkers: function() {
        var table = document.getElementById("gameTable");
        var targetTDs = table.querySelectorAll('td');

            for (var i = 0; i < targetTDs.length; i++) {
                var isPlacerMarker = targetTDs[i].style.backgroundColor == "gray";
                if (isPlacerMarker) {
                     targetTDs[i].style.backgroundColor = "white";
                 }
            }
        },
        selectEndLocation: function(possiblePositions, location) {
            var end;
            var table = document.getElementById("gameTable");
            var targetTDs = table.querySelectorAll('td');

            for (let i = 0; i < targetTDs.length; i++) {

                for (let j = 0; j < possiblePositions.length; j++) {
                    if (targetTDs[i].id == possiblePositions[j]) {
                        var id = targetTDs[i].id;
                        var self = this;

                        targetTDs[i].onclick = (function(id){
                            return function(){
                                var id1 = id;
                                var finalPositions = self.calculateFinalPosition(location, id1);

                                if (!self.canPlaceShip(finalPositions)) {
                                    alert("You cannot place a ship here!");
                                } else {
                                    document.getElementById(id).style.background = "red";
                                    self.setFinalShipPositions(finalPositions);
                                    for (var i = 0; i < self.ships.length; i++) {
                                        if (self.shipType == self.ships[i].type) {
                                            self.ships[i].placing = false;
                                            self.ships[i].placed = true;
                                            self.placing = false;
                                        }
                                    }
                                    self.clearPlacerMarkers();
                                }
                            }
                        })(id);
                    }
                }
            }

            return end;
        },
        calculateFinalPosition(startLocation, endLocation) {
            var finalPositions = [];
            var missingLetters = [];
            var firstLetter = startLocation[0];
            var endLetter = endLocation[0];
            var list = [];

            if (startLocation.length > 2) {
                var firstNumber = "10";
            } else {
                var firstNumber = startLocation[1];
            }

            if (endLocation.length > 2) {
                var endNumber = "10";
            } else {
                var endNumber = endLocation[1];
            }

            if (firstLetter == endLetter) {
                var missingLetters = firstLetter;
            } else {
                if (firstLetter.charCodeAt(firstLetter) < endLetter.charCodeAt(endLetter)) {
                    for (var i = firstLetter.charCodeAt(firstLetter); i <= endLetter.charCodeAt(endLetter); i++) {
                        if (i <= 74 && i >= 65) {
                            missingLetters.push(String.fromCharCode(i))
                        }
                    }
                } else {      
                    for (var i = endLetter.charCodeAt(endLetter); i <= firstLetter.charCodeAt(firstLetter); i++) {
                        if (i <= 74 && i >= 65) {
                            missingLetters.push(String.fromCharCode(i))
                        }
                    }
                }
            }

            if (parseInt(firstNumber) < parseInt(endNumber)) {
                for (var i = parseInt(firstNumber); i <= parseInt(endNumber); i++) {
                    list.push(i);
                }
            } else {
                for (var i = parseInt(endNumber); i <= parseInt(firstNumber); i++) {
                    list.push(i);
                }
            }

            for (var i = 0; i < list.length; i++) {
                for (var j = 0; j < missingLetters.length; j++) {
                    var newCoordinate = missingLetters[j] + list[i];
                    finalPositions.push(newCoordinate);
                }
            }

            return finalPositions;
        },
        setFinalShipPositions: function(finalPositions) {
            var allPositions = this.getAllPlacedShipPositions();

            for (var i = 0; i < allPositions.length; i++) {
                var alreadyPlacedPosition = allPositions[i];

                for (var j = 0; j < finalPositions.length; j++) {
                    if (alreadyPlacedPosition == finalPositions[j]) {
                        return false;
                    } 
                }
            }

            for (var i = 0; i < this.ships.length; i++) {
                if (this.shipType === this.ships[i].type) {
                    this.ships[i].positions = finalPositions;
                }
            }

            this.showFinalShipPositions(finalPositions);

            return finalPositions;
        },
        canPlaceShip: function(finalPositions) {
            var allPositions = this.getAllPlacedShipPositions();

            for (var i = 0; i < allPositions.length; i++) {
                var alreadyPlacedPosition = allPositions[i];

                if (alreadyPlacedPosition == finalPositions) {
                    return false;
                }

                for (var j = 0; j < finalPositions.length; j++) {
                    if (alreadyPlacedPosition == finalPositions[j]) {
                        return false;
                    } 
                }
            }

            return true;
        },
        showFinalShipPositions: function(finalPositions) {
            var table = document.getElementById("gameTable");
            var targetTDs = table.querySelectorAll('td');

            for (var i = 0; i < targetTDs.length; i++) {
                var tdId = targetTDs[i].id;

                for (var j = 0; j < finalPositions.length; j++) {
                    if (tdId == finalPositions[j]) {
                        document.getElementById(tdId).style.background = "red";
                    }
                }
            }

            this.getAllPlacedShipPositions();
        },
        showShipPositions: function(data) {
            var ships = data.ships;
            var shipLocations = [];

            for (var i = 0; i < ships.length; i++) {
                var ship = ships[i];

                ship.locations.forEach(function(coordinate) {
                    shipLocations.push(coordinate);
                })
            }

            this.showFinalShipPositions(shipLocations);
        },
        showAllShips: function(allShips) {
            var table = document.getElementById("gameTable");
            var targetTDs = table.querySelectorAll("td");

            for (var i = 0; i < targetTDs.length; i++) {
                var tdId = targetTDs[i].id;

                for (var j = 0; j < allShips.length; j++) {
                    if (tdId == allShips[j]) {
                        document.getElementById(tdId).style.background = "red";
                    }
                }
            }
        },
        getAllPlacedShipPositions: function() {
            var allPlacedShipPositions = [];

            for (var i = 0; i < this.ships.length; i++) {
                if (this.ships[i].positions.length != 0) {
                    for (var j = 0; j < this.ships[i].positions.length; j++) {
                        allPlacedShipPositions.push(this.ships[i].positions[j]);
                    }
                } 
            }

            this.showAllShips(allPlacedShipPositions);

            return allPlacedShipPositions;
        },
        selectShipLocation: function(id) {
            if (this.canPlaceShip(id) && this.shipSelected) {
                this.location = id;
    
                if (!this.placing && this.checkIfValidFinalLocation(id)) {
                    this.showStartLocation(id);
                    this.placing = true;
                    this.shipSelected = false;
                }
            }

            return id;
        },
        getShipHits: function(data) {
            var ships = data.ships;
            var hits = [];
            var hitLocations = [];

            for (var i = 0; i < ships.length; i++) {
                hits.push(ships[i].hits);

                for (var j = 0; j < hits.length; j++) {

                    hits[i].forEach(function(hit) {
                        hitLocations.push(hit.location);
                    })
                }
            }

            this.showShipHits(hitLocations)
        },
        showShipHits: function(hits) {
            var table = document.getElementById("gameTable");
            var targetTDs = table.querySelectorAll("td");

            for (var i = 0; i < targetTDs.length; i++) {
                var tdId = targetTDs[i].id;

                for (var j = 0; j < hits.length; j++) {
                    if (tdId == hits[j]) {
                        document.getElementById(tdId).innerHTML = "X";
                        document.getElementById(tdId).style.textAlign = "center";
                    }
                }
            }
        },
        getHitsOnOpponent: function(data) {
            var gamePlayers = data.gamePlayers;
            var hits = [];
            var hitLocations = [];

            for (var i = 0; i < gamePlayers.length; i++) {
                if (gamePlayers[i].id == this.gamePlayerId) {
                    var salvoes = gamePlayers[i].salvoes;
                }
            }

            for (var i = 0; i < salvoes.length; i++) {
                hits.push(salvoes[i].hits);
                salvoes[i].hits.forEach(function(hit) {
                    hitLocations.push(hit.location);
                })
            }

            return hitLocations;
        },
        showHitsOnOpponent: function(hitLocations) {
            var table = document.getElementById('salvoTable');
            var targetTDs = table.querySelectorAll('td');

            for (var i = 0; i < targetTDs.length; i++) {
                var tdId = targetTDs[i].id.substring(10);
        
                for (var j = 0; j < hitLocations.length; j++) {
                    if (tdId == hitLocations[j]) {
                        elementId = "salvoTable" + tdId;
                        document.getElementById(elementId).innerHTML = "X";
                        document.getElementById(elementId).style.textAlign = "center";
                    }
                }
            }
        },
        getOpponentSalvoLocations: function(data) {
            var opponentSalvoes = this.getSalvoesFiredByOpponent(data);
            var opponentSalvoLocations = [];

            opponentSalvoes.forEach(function(salvo) {
                opponentSalvoLocations.push(salvo.locations);
            })

            return opponentSalvoLocations;
        },
        showSalvoesFired: function(data) {
            var salvoes = this.getSalvoesFiredByGamePlayerId(data);
            var table = document.getElementById('salvoTable');
            var targetTDs = table.querySelectorAll('td');
            var salvoLocations = [];

            for (var i = 0; i < salvoes.length; i++) {
                salvoes[i].locations.forEach(function(coordinate) {
                    salvoLocations.push(coordinate);
                })
            }
        
            for (var i = 0; i < targetTDs.length; i++) {
                var tdId = targetTDs[i].id.substring(10);
        
                for (var j = 0; j < salvoLocations.length; j++) {
                    if (tdId == salvoLocations[j]) {
                        targetTDs[i].style.backgroundColor = "yellow";
                    }
                }
            }
        }, 
        selectSalvoLocation: function(id) {
            if (this.turnToPlaceSalvoes == true) {
                var coordinate = this.getCoordinate(id);
                var element = this.getElement(id);
    
                if (this.canPlaceSalvo(coordinate)) {
                    this.setSalvoPosition(coordinate, element);
                } else if (!this.canPlaceSalvo(coordinate)) {
                    this.removeSalvoPosition(coordinate, element);
                }
            } else {
                alert("It's not your turn to place a salvo!")
            }
        },
        getCoordinate: function(id) {
            var elementId = document.getElementById(id).id;
            var coordinate = elementId.substring(10);

            return coordinate;
        },
        getElement: function(id) {
            var element = document.getElementById(id);

            return element;
        },
        canPlaceSalvo: function(coordinate) {
            var allPositions = this.salvo.locations;

            if (allPositions.length > 0) {
                for (var i = 0; i < allPositions.length; i++) {
                    var alreadyPlacedPosition = allPositions[i];
    
                    if (alreadyPlacedPosition == coordinate) {
                        return false;
                    }
                }
            }

            return true;
        },
        setSalvoPosition: function(coordinate, element) {
            var salvo = this.salvo;

            if (salvo.locations.length < 5) {
                salvo.locations.push(coordinate);
                this.changeElementBackgroundColor(element, "black");
            } else {
                alert("You've already selected the maximum 5 shots!")
            }
        },
        removeSalvoPosition: function(coordinate, element) {    
            var index = this.salvo.locations.indexOf(coordinate);

            if (index > -1) {
                this.salvo.locations.splice(index, 1);
            }

            this.changeElementBackgroundColor(element, "white");

            alert("Shot " + coordinate + " removed");
        },
        changeElementBackgroundColor: function(element, color) {
            element.style.backgroundColor = color;
        },
        getSalvoes: function() {
            this.salvoes.push(this.salvo);
            
            return this.salvoes;
        },
        checkIfValidFinalLocation: function(location) {
            var endpoints = this.calculateShipEndLocation(location);
            var validPositionChecks = [];
            var self = this;

            endpoints.forEach(function(endpoint) {
                var finalPositions = self.calculateFinalPosition(location, endpoint);
                var result = self.canPlaceShip(finalPositions);
                validPositionChecks.push(result);
            })

            if (!validPositionChecks.includes(true)) {
                alert("There are no valid positions available here!")
                return false;
            }

            return true;
        },
        calculateShipEndLocation: function(id) {
            var possiblePosition = this.shipLength - 1;
            var positions = [];

            if (id.length > 2) {
                var numberCoordinate = "10";
            } else {
                var numberCoordinate = id[1];
            }

            var letterCoordinate = id[0];

            var horizontal1 = parseInt(numberCoordinate) + parseInt(possiblePosition);
            var horizontal2 = parseInt(numberCoordinate) - parseInt(possiblePosition);

            if (horizontal1 <= 10 && horizontal1 >= 1) {
                positions.push(letterCoordinate + horizontal1);
            }

            if (horizontal2 <= 10 && horizontal2 >= 1) {
                positions.push(letterCoordinate + horizontal2);
            }

            var nextLetter = String.fromCharCode(letterCoordinate.charCodeAt(letterCoordinate) + parseInt(possiblePosition));
            var previousLetter = String.fromCharCode(letterCoordinate.charCodeAt(letterCoordinate) - parseInt(possiblePosition));

            if (nextLetter.charCodeAt(0) <= 74 && nextLetter.charCodeAt(0) >= 65) {
                positions.push(nextLetter + numberCoordinate);
            }

            if (previousLetter.charCodeAt(0) <= 74 && previousLetter.charCodeAt(0) >= 65) {
                positions.push(previousLetter + numberCoordinate);
            }

            this.possibleShipPositions = positions;

            return positions;
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
                window.location.href = "/web/games.html";
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
        getShipLocations: function() {
            var ships = this.ships;
            var shipLocations = [];
        
            for (var i = 0; i < ships.length; i++) {
                var ship = ships[i];
                shipLocations.push({type: ship.type, locations: ship.positions});
            }

            return shipLocations;
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
        changeGamePlayerHeader: function(data) {
            var emails = [];
            var gamePlayerHeader = document.getElementById("gamePlayerHeader");

            if (data.gamePlayers[0].id == this.gamePlayerId) {
                var email = data.gamePlayers[0].player.email;
                emails.push(email + " (you)");

                if (data.gamePlayers[1]) {
                    var emailOpponent = data.gamePlayers[1].player.email;
                    emails.push(" " + emailOpponent + " (opponent) ");
                }
            } else if (data.gamePlayers[1]) {
                var email = data.gamePlayers[1].player.email;
                emails.push(email + " (you)");
                var emailOpponent = data.gamePlayers[0].player.email;
                emails.push(" " + emailOpponent + " (opponent) ");
            }

            gamePlayerHeader.innerHTML = emails;
        },
    },
    created: function () {
        this.fetchData();
    },
});