import firebase from 'firebase/app';
import 'firebase/auth';

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
firebase.initializeApp(firebaseConfig);
const auth = firebase.auth();

const signInGoogle= () => {
    const provider = new firebase.auth.GoogleAuthProvider();
    return auth.signInWithPopup(provider);
}