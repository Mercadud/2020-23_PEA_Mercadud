import React, { useContext, useState } from "react";
import { myContext } from "./App";

export default function ActorSelect() {
    const c = useContext(myContext);
    let [actor, setActor] = useState('');

    function searchByActor(_) {
        c.setSearchParam(actor);
        c.setGetType('C');
        c.refresh();
    }

    return (
        <div>
            <input placeholder="Actor Name" value={actor} onInput={e => setActor(e.target.value)} />
            <button onClick={searchByActor} >Search by Actor Name</button>
        </div>);
}