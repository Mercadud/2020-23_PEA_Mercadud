import React from "react";

export default function Genres({genres}) {
    return (
        <div className="list">
            <h4>Genres:</h4>
            <div>
                {
                    genres.map((g, i) => <p key={i}>{g}</p>)
                }
            </div>
        </div>
    )
}