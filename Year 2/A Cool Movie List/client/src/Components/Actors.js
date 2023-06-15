import React from "react";

export default function Actors({actors}) {
    return (
        <div className="list">
            <h4>Actors:</h4>
            <div>
                {
                    actors.map((a, i) => <p key={i}>{a}</p>)
                }
            </div>
        </div>
    )
}