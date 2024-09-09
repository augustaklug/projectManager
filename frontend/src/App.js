import React from 'react';
import {Button} from "@/components/ui/button"
import {useInvalidateUserCacheOnProfileRoute} from "./services/userService";

function App() {
    useInvalidateUserCacheOnProfileRoute();
    return (
        <div className="App">
            <header className="App-header">
                <h1>Project Manager</h1>
                <Button>Click me</Button>
            </header>
        </div>
    );
}

export default App;