import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import { BrowserRouter,Routes, Route } from 'react-router-dom'
import Login from './Public/Login.jsx'
import Register from './Public/Register.jsx'
import Home from './Public/Home.jsx'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
    <Routes>
      <Route path='/' element={<App/>}>
       <Route path='/' element={<Home/>}></Route>
       <Route path='/login' element={<Login/>}></Route>
      <Route path='/register' element={<Register/>}></Route>
      </Route> 
    </Routes>
    </BrowserRouter>
  </React.StrictMode>,
)
