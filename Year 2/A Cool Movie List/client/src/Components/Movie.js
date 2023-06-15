import React, { useContext } from "react";
import Genres from "./Genres";
import Actors from "./Actors";
import '../styles/Movie.css';
import { myContext } from "./App";


export default function Movie({Title, Year, Runtime, Revenue, Genre, Actor}) {
    const c = useContext(myContext);

    function hide(_) {
        c.setMovieSelected(-1);
    }
    
    return (
        <div id="MovieSelected" onClick={hide}>
            <h3>{Title} ({Year})</h3>
            <p>Runtime : {Runtime} min</p>
            <p>Revenue : ${Revenue} million</p>
            <Genres genres={Genre} />
            <Actors actors={Actor} />
        </div>
    );
}
