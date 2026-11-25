import React from 'react'

function LibraryBooks() {
    const books =[
        {
        code:"WEB 001",
        name:"Full stack Development ",
        price:"500"
},
      {
        code:"SEENG 002",
        name:"Softawere Enginnerinng ",
        price:"50"
},
      {
        code:"Math 003",
        name:"Mathematics ",
        price:"100"
}
]

  return (
    <div>
      <h2>List of books</h2>
        <ul>{
            books.map(book => (
    <li key={book.code}>
        This is the name {book.name} and its price is {book.price}
    </li>
))
            }
          
        </ul>
        <div>
          <h1> The total price for {books.length} books is {books.reduce((a, b) => a + Number(b.price), 0)}</h1>
        </div>
    </div>
  )
}

export default LibraryBooks
