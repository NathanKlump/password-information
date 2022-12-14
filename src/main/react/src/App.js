import React from 'react'
import { useEffect, useState } from "react";

import './components/css/App.css'
import { get_lastChange } from './components/api/api.js'


function App(props) {
  const [changedLast, setChangedLast] = useState('days');

  function getDaysSinceLastChange(data){
    let daySinceLastChange = Math.round(((((new Date()).getTime()  / 1000 / 60 / 60) + 12) / 24) - parseInt(data));
    setChangedLast(daySinceLastChange);
  }

  useEffect(
    () => {
      get_lastChange()
      .then(
        (data) => {
          getDaysSinceLastChange(data.lastChange);
        }
      ).catch(
        (e) => {
        }
      )
    }
  );

  return (
            <div className="App">
                <div className="row">
                    <div className="col-xs-12">
                        <div id="password-container" className={(changedLast<90) ? "alert" : "alert-danger"}>
                            It has been <span id="password-days">{changedLast}</span> day(s) since you
                            changed your password.
                        </div>
                    </div>
                </div>

                <div className="row">
                    <div className="col-xs-12">
                        <div className="alert alert-info">
                            Oakland University recommends changing your password every 90
                            days.
                            <a
                                className="alert-link"
                                href="https://netid.oakland.edu"
                                target="_blank"
                                rel="noopener noreferrer"
                                aria-describedby="new-window-0"
                            >
                                Click Here to change your password.
                            </a>
                        </div>
                    </div>
                </div>
            </div>
  );
}

export default App;

