import React from 'react';
import ReactDom from 'react-dom';
import './index.css';
import store from './reducer/index.jsx';
import expect from 'expect';
import deepFreeze from "deep-freeze";

console.log(store);
// store.dispatch({ type: 'increment' });
// store.subscribe(() => {
//     document.body.innerText = store.getState();
// })
// document.addEventListener('click', () => {
//     store.dispatch({ type: 'increment' });
// });

const Counter = ({
    value,
    onIncrement,
    onDecrement
}) => (
        <div className="counter">
            <h1>{value}</h1>
            <button onClick={onIncrement}>+</button>
            <button onClick={onDecrement}>-</button>
        </div>
    );

const render = () => {
    ReactDom.render(
        <Counter
            value={store.getState()}
            onIncrement={() => store.dispatch({ type: 'increment' })}
            onDecrement={() => store.dispatch({ type: 'decrement' })}
        />,
        document.getElementById('root')
    )
};

store.subscribe(render);

render();



console.log('---------------------------');


const todo = (state, action) => {
    switch (action.type) {
        case 'ADD_TODO':
            return {
                id: action.id,
                text: action.text,
                completed: false
            };
        case 'TOGGLE_TODO':
            if (state.id !== action.id) {
                return state;
            }
            return {
                ...state,
                completed: !state.completed
            }
        default:
            return state;
    }
};

const todos = (state = [], action) => {
    switch (action.type) {
        case 'ADD_TODO':
            return [
                ...state,
                todo(null, action)
            ]
        case 'TOGGLE_TODO':
            return state.map(t => todo(t, action))
        default:
            return state;
    }
};

const testToDo = () => {
    const stateBefore = [
        {
            id: 1,
            text: 't1',
            completed: false
        },
        {
            id: 2,
            text: 't2',
            completed: false
        }
    ];
    const action = {
        id: 2,
        type: 'TOGGLE_TODO'
    };
    const stateAfter = [
        {
            id: 1,
            text: 't1',
            completed: false
        },
        {
            id: 2,
            text: 't2',
            completed: true
        }
    ];

    deepFreeze(stateBefore);
    expect(todos(stateBefore, action)).toEqual(stateAfter);
};

testToDo();
console.log('All test passed');
















// class Square extends React.Component {
//     render() {
//         return (
//             <button className="square" onClick={this.props.onClick}>
//                 {this.props.value}
//             </button>
//         )
//     }
// };

function Square(props) {
    return (
        <button className="square" onClick={props.onClick}>
            {props.value}
        </button>
    )
}

function calculateWinner(square) {
    const line = [
        [0, 1, 2],
        [3, 4, 5],
        [6, 7, 8],
        [0, 3, 6],
        [1, 4, 7],
        [2, 5, 8],
        [0, 4, 8],
        [2, 4, 6]
    ];
    for (let i = 0; i < line.length; i++) {
        const [a, b, c] = line[i];
        if (square[a] && square[a] === square[b] && square[a] === square[c]) {
            return square[a];
        }
    }
    return null;
}


class Board extends React.Component {
    renderSquare(i) {
        return <Square value={this.props.squares[i]} onClick={() => { this.props.onClick(i) }} />
    }
    render() {
        return (
            <div className="mainContent">
                <div className="board-row">
                    {this.renderSquare(0)}
                    {this.renderSquare(1)}
                    {this.renderSquare(2)}
                </div>
                <div className="board-row">
                    {this.renderSquare(3)}
                    {this.renderSquare(4)}
                    {this.renderSquare(5)}
                </div>
                <div className="board-row">
                    {this.renderSquare(6)}
                    {this.renderSquare(7)}
                    {this.renderSquare(8)}
                </div>
            </div>
        )
    }
};

class Game extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            history: [{
                squares: Array(9).fill(null)
            }],
            xIsNext: true,
            stepNumber: 0
        }
    }
    handleClick(i) {
        console.log(i);
        const history = this.state.history.slice(0, this.state.stepNumber + 1);
        const current = history[history.length - 1];
        const squares = current.squares.slice(0);
        if (calculateWinner(squares) || squares[i]) {
            return;
        }
        squares[i] = this.state.xIsNext ? 'X' : 'O';
        this.setState({
            history: history.concat([{
                squares: squares
            }]),
            stepNumber: history.length,
            xIsNext: !this.state.xIsNext
        });
        console.log(this.state);
    }
    jumpTo(i) {
        this.setState({
            stepNumber: i
        });
    }
    render() {
        const history = this.state.history;
        const current = history[this.state.stepNumber];
        const winner = calculateWinner(current.squares);
        let squares = current.squares.slice(0);

        let status = winner ? 'Winner is ' + winner : ('Next Player: ' + (this.state.xIsNext ? 'X' : 'O'));

        const movie = history.map((n, i) => {
            const desc = i ? 'move to ' + i : 'move to start';
            return (
                <li key={i}>
                    <button onClick={() => this.jumpTo(i)}>{desc}</button>
                </li>
            )
        });

        return (
            <div className="game">
                <div className="game-board">
                    <Board squares={squares} onClick={(i) => this.handleClick(i)} />
                </div>
                <div className="game-info">
                    <div>{status}</div>
                    <ol>{movie}</ol>
                </div>
            </div>
        )
    }
}

// ReactDom.render(
//     <Game />,
//     document.getElementById('root')
// );






