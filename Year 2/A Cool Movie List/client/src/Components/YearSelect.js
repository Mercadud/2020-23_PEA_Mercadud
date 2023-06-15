import React, { useContext, useState } from "react";
import { myContext } from "./App";

export default function ActorSelect() {

    const c = useContext(myContext);
    let [year, setYear] = useState(2018);

    function searchByYear(e) {
        c.setSearchParam(year);
        c.setGetType('Y');
        c.refresh();
    }

    return (
        <div>
            <input type="number" min="1950" value={year} onInput={e => setYear(e.target.value)} />
            <button onClick={searchByYear}>Search by Year</button>
        </div>);
}