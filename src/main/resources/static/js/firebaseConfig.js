// Import the functions you need from the SDKs you need
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.8.1/firebase-app.js";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
    apiKey: "AIzaSyDlh3LtIniut_lydGKYpDmFYtjtWQ8k0qw",
    authDomain: "asset-calculator.firebaseapp.com",
    projectId: "asset-calculator",
    storageBucket: "asset-calculator.appspot.com",
    messagingSenderId: "505275850922",
    appId: "1:505275850922:web:25da447155dc7fb24e30d3",
    measurementId: "G-7E4F2FQYWF"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);