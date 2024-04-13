import React from 'react'
import { Link } from 'react-router-dom'
import { BiSearch, BiUserCircle } from 'react-icons/bi';
import { IoCartOutline } from 'react-icons/io5';
import { LuBoxes } from 'react-icons/lu';
import { HiMiniBars3BottomLeft } from 'react-icons/hi2';
import { GoHeart } from 'react-icons/go';

 const Headers = () => {
  const user={
    userId:123,
    username:"abc",
    role:"SELLER",
    aunthenticated:false,
    accessExpiration:3600,
    refershExpiration:1296000

  }
   
  const{ username,role,aunthenticated}=user;
  return (
    <nav className='bg-white shadow-md text-slate-100 py-2 text-xl'>
      <div className='w-11/12 flex items-center justify-evenly'>
        { /* logo and Link block */}
        <div className='flex justify-center items-center w-3/6'>

          {/* logo */}
          <Link to={"/"} className='w-60'>
            <img src='/src/Images/flipkart-logo.svg' alt="" className='w-full'/>
          </Link>

            {/* Search bar */}
            <div className='bg-blue-100 w-full rounded-xl mx-10 flex justify-center'>
              <div className='text-slate-500 flex justify-center items-center w-7 text-2xl m-2 mr-0'><BiSearch/></div>
              <input type='text' className='p-2 bg-transparent w-full focus:outline-none text-slate-700 placeholder:text-slate-500'
              placeholder='Search for mpbile,laptops, ...'/>
            </div>
        </div>

        {/* Nav Links*/}
        <div  className='text-slate-600 flex justify-evenly items-center w-2/6'>
          <Link to={aunthenticated ? "/account" : "/login"}>{aunthenticated ? username
          :"Login"} </Link>
          {/* Optional links based on role*/}
          {(aunthenticated && role === "CUSTOMER")
          ? <div className='flex justify-center items-center'>
          <HeaderLink icon={<IoCartOutline/>} linkName={"Cart"} to={"/cart"}/>
          <HeaderLink icon={<GoHeart/>} linkName={"Wishlist"} to={"/wishlist"}/>
            </div>
            :(aunthenticated && role === "SELLER") ?
            <HeaderLink icon={<LuBoxes/>} linkName={"Orders"} to={"/orders"}/>
            :(!aunthenticated) &&
            <HeaderLink icon={<BiUserCircle/>} linkName={"Become a Seller"} to={"/register"}/> 
          }
          <Link><HiMiniBars3BottomLeft/></Link>
        </div>
        </div>
        </nav>
  )
}

export default Headers

export const HeaderLink=({icon,linkName,to})=>
{
  return (
    <Link to={to} className='text-slate-600 flex justify-center items-center'>
      <div className='mt-1 mr-2'>{icon}</div>
      <div>{linkName}</div>
    </Link>

  )
}