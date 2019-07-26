/**
 * variables globales
 */


var game;

var trampas = false;

var shipSize = {
    Portaaviones: 5,
    Cruceros: 4,
    Destructores: 3,
    Fragatas: 2,
    Submarinos: 1
};

var timeShoot;

/**
 * Objetos
 */



function Flota(name, quantity, size) {
    this.name = name;
    this.quantity = quantity;
    this.size = size;
}

function BattleResponse(userBoard, launchBoard, matrixSize, stock, endGame, logBattle) {

    this.userBoard = userBoard;
    this.launchBoard = launchBoard;
    this.matrixSize = matrixSize;
    this.stock = stock;
    this.endGame = endGame;
    this.logBattle = logBattle;
}

function LaunchRocket(x, y) {
    this.x = x;
    this.y = y;
}

/**
 * Generar codigo html
 *
 */


function loadLog(battleLog) {

    var log = document.getElementById('log');

    clearDiv(log);

    var textTittle = document.createTextNode('Battle Log');
    var tittle = document.createElement('h3');
    tittle.setAttribute('class', 'text-center');
    tittle.appendChild(textTittle);

    log.appendChild(tittle);

    if (battleLog) {

        var pOutput = document.createElement('pre');
        pOutput.setAttribute('class', 'text-left overflow-auto vh-100');
        Object.keys(battleLog).forEach(function (logIndex) {
            var register = battleLog[logIndex];
            var pText = document.createTextNode(register + '\n');
            pOutput.insertBefore(pText, pOutput.firstChild);
        });
        log.appendChild(pOutput);
    }

}

function loadBoard(userBoard, user) {
    var board = document.createElement('table');
    var bodyBoard = document.createElement('tbody');

    for (var x = 0; x < userBoard.length; x++) {
        var hilera = document.createElement('tr');

        hilera.appendChild(celdaCoordenada(x));
        for (var y = 0; y < userBoard[x].length; y++) {
            hilera.appendChild(getCelda(userBoard[x][y], x, y, user));
        }
        bodyBoard.insertBefore(hilera, bodyBoard.firstChild);
    }
    bodyBoard.appendChild(getHileraCoordenada(userBoard.length));
    board.appendChild(bodyBoard);
    board.setAttribute('class', 'table table-bordered');
    return board;
}

function getHileraCoordenada(size) {
    var hilera = document.createElement('tr');

    var eje = document.createElement('td');
    var textoCelda = document.createTextNode('axis');
    eje.appendChild(textoCelda);
    hilera.appendChild(eje);

    for (var y = 0; y < size; y++) {
        hilera.appendChild(celdaCoordenada(y));
    }
    return hilera;
}

function celdaCoordenada(position) {
    var celda = document.createElement('td');
    var textoCelda = document.createTextNode(position);
    celda.appendChild(textoCelda);
    return celda;
}

function getCelda(infoCelda, x, y, user) {
    var celda = document.createElement('td');

    if (infoCelda['impact']) {
        var texto = trampas ? infoCelda['shipSize'] : 'X';
        var textoCelda = document.createTextNode(texto);

        celda.appendChild(textoCelda);
    }
    var background;
    if (user === 'USER') {
        background = getUserBackColor(infoCelda['status'], infoCelda['ship'], infoCelda['impact']);
    } else {
        background = getLaunchBackColor(infoCelda['status']);
    }

    celda.setAttribute('class', background);
    celda.setAttribute('onclick', 'shoot(' + x + ',' + y + ');');
    return celda;

}

function getLaunchBackColor(status) {
    if (status === 'INIT') {
        return 'bg-light';
    } else if ((status === 'WATTER')) {
        return 'bg-info';
    } else if ((status === 'SUNKEN')) {
        return 'bg-danger';
    } else if (status === 'TOUCH') {
        return 'bg-warning';
    }
}


function getUserBackColor(status, ship, impact) {
    if (!ship && !impact) {
        return 'bg-info';
    } else if (!ship && impact) {
        return 'bg-success';
    } else if (ship && !impact) {
        return 'bg-dark';
    } else if (ship && impact) {
        if (status === 'TOUCH') {
            return 'bg-warning';
        } else {
            return 'bg-danger';
        }
    }
}

function clearDiv(divToClean) {
    // var divToClean = document.getElementById(id);
    while (divToClean.firstChild) {
        divToClean.removeChild(divToClean.firstChild);
    }
}

function getTittle(titulo) {
    var textTittle = document.createTextNode(titulo);
    var tittle = document.createElement('h3');
    tittle.setAttribute('class', 'text-center');
    tittle.appendChild(textTittle);
    return tittle;
}


/**
 *
 * Lógica del front
 */

function standarShips() {
    var stock = [];
    stock.push(new Flota('Portaaviones', 1, 5));
    stock.push(new Flota('Cruceros', 2, 4));

    stock.push(new Flota('Destructores', 3, 3));
    stock.push(new Flota('Fragatas', 2, 2));
    stock.push(new Flota('Submarinos', 4, 1));
    return stock;
}

function getBoatFleet(ships) {
    var stock = [];
    for (var i = 0; i < ships.length; i++) {
        var shipform = ships[i];
        if (shipform['id'] !== 'boardSize') {
            var shipType = shipform['id'];
            var quantity = shipform['value'] === '' ? shipform['placeholder'] : shipform['value'];
            stock.push(new Flota(shipType, quantity, shipSize[shipType]));
        }
    }
    return stock;
}

function startForm() {
    var gameForm = document.forms['gameForm'];
    console.log(gameForm);
    var size = gameForm.getElementsByTagName('input')['boardSize'].value;
    size = size === '' ? 0 : size;
    var ships = gameForm.getElementsByTagName('input');

    var newGame = {'matrixSize': size, 'stock': getBoatFleet(ships)};
    start(newGame);
}

function startQuickly() {
    var newGame = {'matrixSize': 10, 'stock': standarShips()};
    start(newGame);
}

function jumpShootCpu() {
   console.log('has tardado mas de 4 segundos');
    launch(-1, -1);
}

function updateGame(battleResponse) {
    game = battleResponse;
    if (game.endGame) {
        end();
    }
    loadLog(game.logBattle);

    // borro y pinto los tableros
    var enemyBoard = document.getElementById('launchBoard');
    clearDiv(enemyBoard);
    enemyBoard.appendChild(loadBoard(game.launchBoard, 'ENEMY'));

    // Delay para que parezca que la Cpu esta pensando el ataque
    window.setTimeout(loadCpuAttack, 2000);
    timeShoot = window.setInterval(jumpShootCpu, 4000);
}

function resumeGame(battleResponseList) {
    game = battleResponseList['USER'];
    loadLog(game.logBattle);
    var boards = [battleResponseList['USER'].launchBoard
        , battleResponseList['CPU'].launchBoard
        , battleResponseList['USER'].userBoard
        , battleResponseList['CPU'].userBoard];
    var owners = ['ENEMY', 'ENEMY', 'USER', 'USER'];
    var resumeContent = document.getElementById('resume');
    clearDiv(resumeContent);
    for (var i = 0; i < 4; i++) {
        var column = document.createElement('div');
        column.setAttribute('class', 'col-5 p-0 m-auto');
        column.appendChild(getTittle(owners[i]));
        column.appendChild(loadBoard(boards[i], owners[i]));
        resumeContent.appendChild(column);
    }
    resumeContent.appendChild(getButtonClear('resume'));
}

function getButtonClear(id) {
    var button = document.createElement('button');
    button.setAttribute('type', 'button');
    button.setAttribute('class', 'btn btn-info btn-lg btn-block');
    button.setAttribute('onclick', 'callClear("' + id + '");');
    button.appendChild(document.createTextNode('Clear'));
    return button;
}

function callClear(id) {
    var box = document.getElementById(id);
    clearDiv(box);
}

function loadCpuAttack() {
    var userBoard = document.getElementById('userBoard');
    clearDiv(userBoard);
    userBoard.appendChild(loadBoard(game.userBoard, 'USER'));
}

function parseToJson(T) {
    return JSON.stringify(T);
}

function shoot(x, y) {
    if (!(game.launchBoard[0] !== undefined && game.launchBoard[x][y]['impact'])) {
        launch(x, y);
    } else {
        alert('Ya ha lanzado un ataque a las coordenadas --> X: ' + x + '  Y: ' + y);
    }

}

function launchForm() {
    var coordenates = document.forms['rocketForm'];
    var x = coordenates['axisX'].valueAsNumber;
    var y = coordenates['axisY'].valueAsNumber;

    if (!(game.launchBoard[0] !== undefined && game.launchBoard[x][y]['impact'])) {
        launch(x, y);
    } else {
        alert('Ya ha lanzado un ataque a las coordenadas --> X: ' + x + '  Y: ' + y);
    }
}

function activarTrampas() {
    trampas = !trampas;
    var infoBox = document.getElementById('infoTrampas');
    infoBox.setAttribute('placeholder', trampas ? 'Activadas' : 'Desactivadas');
}

/**
 *  Peticiones HTTP
 */
function end() {
    alert('END GAME');
    getInfo();
}

function getInfo() {
    $.ajax({
        url: 'http://localhost:8080/battle/print',
        dataType: 'json',
        crossDomain: true,
        success: function (values) {
            console.log(values);
            resumeGame(values);
        }
    });
}

function launch(x, y) {
    clearInterval(timeShoot);

    var rocket = new LaunchRocket(x, y);
    console.log(parseToJson(rocket));

    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/battle/launch',
        data: parseToJson(rocket),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        crossDomain: true,
        success: function (values) {
            console.log(values);
            updateGame(values);
        },
        error: function (error) {
            // console.log(error);
            console.log(error['responseJSON']);
            alert('No existe la coordenada:\n\t\t' + error['responseJSON']['error_param'] + '   ' + error['responseJSON']['error_value']);
        }
    });

}


function start(newGame) {

    //var newGame = {'matrixSize': 10, 'stock': standarShips()};
    console.log(parseToJson(newGame));
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/battle/start',
        data: parseToJson(newGame),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        crossDomain: true,
        success: function (values) {
            console.log(values);
            updateGame(values);
        },
        error: function (error) {
            console.log(error);
            console.log(error['responseJSON']);
            //alert('No existe la coordenada:\n\t\t' + error['responseJSON']['error_param'] + '   ' + error['responseJSON']['error_value']);
        }
    });
}


window.onload = function () {
    size = 10;
    stock = standarShips();

    game = new BattleResponse(new Array(size), new Array(size), size, stock, false, new Object(null));
    loadLog(game.logBattle);
};