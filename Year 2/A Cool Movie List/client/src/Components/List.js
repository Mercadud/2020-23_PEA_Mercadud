import React, { useContext } from 'react';
import "../styles/List.css";
import { myContext } from './App';


export default function List({movies}) {
    const c = useContext(myContext);

    function selectMovie(e) {
        c.setMovieSelected(e.target.className);
    }

    return (
        <div id="List">
            {
                movies.map(mov => <div className={mov.Key} key={mov.Key} onClick={selectMovie}>
                    <h3 className={mov.Key}>{mov.Title} {mov.Year ? `(${mov.Year})` : null}</h3>
                </div>)
            }
        </div>
    )
}
