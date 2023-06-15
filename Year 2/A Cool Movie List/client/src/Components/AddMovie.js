import React, { useState, useContext } from 'react';
import "../styles/AddMovie.css";
import { myContext } from './App';

export default function AddMovie() {
    const c = useContext(myContext)
    let [title, setTitle] = useState('');
    let [year, setYear] = useState(2018);
    let [genres, setGenres] = useState('');
    let [actors, setActors] = useState('');
    let [runtime, setRuntime] = useState(0);
    let [revenue, setRevenue] = useState(0);

    console.log(c);
    function addMovieToServer(e) {
        e.preventDefault();
        let isValid = true;
        if (title.trim().length < 1 || genres.trim().length < 1 || actors.trim().length < 1)
            isValid = false;
        if (isValid) {
            let thing = {
                "Title": title,
                "Genre": genres.split(','),
                "Actors": actors.split(','),
                "Year": year,
                "Runtime": runtime,
                "Revenue": revenue
            };

            console.log(thing);
            fetch('/movies', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(thing),
            }).then(res => {
                if (res) {
                    alert('movie succesfully added');
                    setTitle('');
                    setYear(2018);
                    setGenres('');
                    setActors('');
                    setRuntime(0);
                    setRevenue(0);
                }
                else {
                    alert('movie not added succesfully:(');
                }
            });
        }
    }

    return (
        <div id='addMovie'>
            <h4>Add Movie</h4>
            <div>
                <label>Title: </label>
                <input type="text" size="20" id='titleInput' value={title} onInput={e => setTitle(e.target.value)} />
                <label>Year: </label>
                <input type="number" max={new Date().getUTCFullYear()} min="1950" id='yearInput' value={year} onInput={e => setYear(e.target.value)} />
                <label>Genres: </label>
                <input type="text" size="50" id='genresInput' value={genres} onInput={e => setGenres(e.target.value)} />
                <label>Actors: </label>
                <input type="text" size="50" id='actorsInput' value={actors} onInput={e => setActors(e.target.value)} />
                <label>Runtime: </label>
                <input type="number" min="0" id='runtimeInput' value={runtime} onInput={e => setRuntime(e.target.value)} />
                <label>Revenue: </label>
                <input type="number" min="0" id='revenueInput' value={revenue} onInput={e => setRevenue(e.target.value)} />
            </div>
            <button onClick={addMovieToServer} >Add Movie</button>

        </div>
    );
}