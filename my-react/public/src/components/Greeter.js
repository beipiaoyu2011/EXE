import React, { Component } from 'react';
import config from './config';
import style from '@less/greeter.less';
import dataService from '@src/dataService';



class Greeter extends Component {
    render(){
        dataService.getSecretTypeList({

        });
        return (
            <div className={style.root}>
                {config.greetText}
            </div>
        )
    }
}

export default Greeter;
