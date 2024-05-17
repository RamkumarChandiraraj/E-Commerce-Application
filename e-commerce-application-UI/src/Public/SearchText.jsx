import axios from 'axios'
import React, { useState } from 'react'
import { IoSearchOutline } from 'react-icons/io5'

function SearchText() {
    const [search,setSearch]=useState('')



    console.log(search)
    const handleSubmit =async()=>{
        console.log("hello")
        try{
       const response= await axios.get(`http://localhost:8080/api/ecav1/products?serachText=${search}`,''
        ,{
            headers:{
              'Content-Type': 'application/json'
            },withCredentials:true
          }

        )
        console.log(response)
    }
    catch(error) {
        if (error.response) {
          // Server responded with a status code outside 2xx range
          console.error('Server error:', error.response.data, error.response.status);
        } else if (error.request) { 
          // Server didn't respond 
          console.error('Request failed, could not reach server.'); 
        } else {
          // Error setting up the request
          console.error('Error creating request:', error.message);
        }
      }
    }

  return (
    <div className='flex items-center'> 
    <div className="text-2xl text-slate-500">
      <IoSearchOutline/>
    </div>
    <input
      type="text"
      placeholder="Search the Product Here!..."
      onChange={(e) => {
        setSearch(e.target.value);
      }}
      className="border-0 rounded-xl bg-blue-50 placeholder:text-slate-500 hover:placeholder:text-slate-400 h-full outline-none px-2 py-2 w-full  text-gray-700"
    />
    <button className='bg-blue-500 py-1 ml-7 px-2 rounded-xl'
    onClick={handleSubmit}
    >Search</button>
  </div>
  
  )
}

export default SearchText