import React from 'react'

function LibraryCards({ bookinfo = {} }) {
  const { code = '', name = '', price = '' } = bookinfo

  return (
    <div style={{ border: '3px solid blue', marginTop: '3px', width: '300px', height: '300px' }}>
      <div>{code}</div>
      <div>{name}</div>
      <div>{price}</div>
    </div>
  )
}

export default LibraryCards