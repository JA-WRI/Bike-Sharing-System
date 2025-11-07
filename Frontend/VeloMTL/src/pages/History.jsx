import TripTable from "../components/Table";
import { useState } from "react";

export default function History() {
    const [searchTerm, setSearchTerm] = useState("");
    const [startDateFilter, setStartDateFilter] = useState("");
    const [endDateFilter, setEndDateFilter] = useState("");
    const [bikeFilter, setBikeFilter] = useState("");
    const handleInput = (e) => {
        setSearchTerm(e.target.value);
        console.log(searchTerm);
    }

    const handleStartDateInput = (e) => {
        setStartDateFilter(e.target.value);
        console.log(startDateFilter);
    }

    const handleEndDateInput = (e) => {
        setEndDateFilter(e.target.value);
        console.log(endDateFilter);
    }

    const handleBikeFilterInput = (e) => {
        setBikeFilter(e.target.value);
    }
    return (
        <div>
            <form>
                <label>Search Bar: </label>
                <input id="Trip Search" type="text" placeholder="Trip ID" onInput={handleInput}/>
                <label> Start Date Filter: </label>
                <input id="startDateFilter" type="date" onChange={handleStartDateInput}/>
                <label> End Date Filter: </label>
                <input id="endDateFilter" type="date" onChange={handleEndDateInput}/>
                <label> Bike Type: </label>
                <select id="bikeType" onChange={handleBikeFilterInput}>
                    <option value="">none</option>
                    <option value="regular">Regular</option>
                    <option value="e-Bike">E-Bike</option>
                </select>
            </form>
            <TripTable
            search={searchTerm}
            startDateFilter={startDateFilter}
            endDateFilter={endDateFilter}
            bikeFilter={bikeFilter}
            />
        </div>
        
    );
}