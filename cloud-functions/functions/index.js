'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


exports.sendPowerNotification = functions.database.ref("/online").onWrite((event) => {
    const data = event.data;
    console.log('Power event triggered');
    if (!data.changed()) {
        return;
    }
    const status = event.data.val();
    const onOff =  status ? "on": "off";

    const payload = {
        notification: {
            title: 'Electricity Monitor - Power status changed',
            body: `Your electricity is now ${onOff}`,
            sound: "default"
        }
    };

    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24 //24 hours
    };
    console.log('Sending notifications');
    return admin.messaging().sendToTopic("Power_Notifications", payload, options);

});
