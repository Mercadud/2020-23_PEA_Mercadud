import React, { useState, useEffect } from 'react';
import '../styles/App.css';
import List from './List';
import AddMovie from './AddMovie';
import { TiFolderAdd } from "react-icons/ti";
import ActorSelect from './ActorSelect';
import YearSelect from './YearSelect';
import Movie from './Movie';

export const myContext = React.createContext('ayy');

export default function App() {
    const [data, setData] = useState([]);
    const [showAddMovie, setShowAddMovie] = useState(false);
    const [getType, setGetType] = useState('A');
    const [searchParam, setSearchParam] = useState('');
    const [refresh, setRefresh] = useState(true);
    const [movieSelected, setMovieSelected] = useState(-1);
    const [movieSelectedData, setMovieSelectedData] = useState(null);
    const [displayMovie, setDisplayMovie] = useState(false);

    useEffect(() => {
        if (refresh) {
            if (getType === 'A' || searchParam === '') {
                fetch("/movies")
                    .then(mov => mov.json())
                    .then(
                        mov => {
                            setData(mov);
                        }
                    )
                    .catch(err => {
                        console.error(`ERR: ${err}`);
                    });
            }
            else if (getType === 'C') {
                fetch(`/actors/${searchParam}`)
                    .then(mov => mov.json())
                    .then(
                        mov => {
                            setData(mov);
                        }
                    )
                    .catch(err => {
                        console.error(`ERR: ${err}`);
                    });
            }
            else if (getType === 'Y') {
                fetch(`/years/${searchParam}`)
                    .then(mov => mov.json())
                    .then(
                        mov => {
                            setData(mov);
                        }
                    )
                    .catch(err => {
                        console.error(`ERR: ${err}`);
                    });
            }
            setRefresh(false);
        }
    }, [refresh]);

    useEffect(() => {
        if (movieSelected != -1 && movieSelected) {
            fetch(`/movies/${movieSelected}`)
                .then(mov => mov.json())
                .then(
                    mov => {
                        if (mov.length > 0) {
                            setMovieSelectedData(mov[0]);
                            setDisplayMovie(true);
                        }
                    }
                )
                .catch(err => {
                    console.error(`ERR: ${err}`);
                });
        }
        else {
            setDisplayMovie(false);
        }
    }, [movieSelected])

    function setAddMovie(e) {
        e.preventDefault();
        setShowAddMovie(!showAddMovie);
    }

    let state = {
        setGetType,
        searchParam,
        setSearchParam,
        setMovieSelected,
        refresh: () => {
            setRefresh(true);
        }
    };

    return (
        <myContext.Provider value={state} >
            <div className="App">
                <header>
                    <h2>Just a Really Cool Movie List</h2>
                    <TiFolderAdd id='addMovieBtn' onClick={setAddMovie} />
                </header>
                {
                    showAddMovie ? <AddMovie id="addMovie" /> : null
                }
                <div id='selectors'>
                <button onClick={(_) => {
                    setGetType('A');
                    setRefresh(true);
                }}> Show All Movies</button>
                <ActorSelect />
                <YearSelect />
                </div>
                {
                    data.length < 1 ? <p>No Movies to display</p> : <List movies={data} />
                }
                {
                    displayMovie ?
                        <Movie
                            Title={movieSelectedData.Title}
                            Year={movieSelectedData.Year}
                            Runtime={movieSelectedData.Runtime}
                            Revenue={movieSelectedData.Revenue}
                            Genre={movieSelectedData.Genre}
                            Actor={movieSelectedData.Actors}
                        /> : null
                }
            </div>
        </myContext.Provider>
    );
}
