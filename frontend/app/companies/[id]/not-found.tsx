import Link from 'next/link'
import React from 'react'

const CompanyNotFound = () => {
  return (
    <main className="h-full p-12">
        <div className='flex flex-col items-center gap-2'>
            <svg className="size-12" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" d="M15.182 16.318A4.486 4.486 0 0 0 12.016 15a4.486 4.486 0 0 0-3.198 1.318M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0ZM9.75 9.75c0 .414-.168.75-.375.75S9 10.164 9 9.75 9.168 9 9.375 9s.375.336.375.75Zm-.375 0h.008v.015h-.008V9.75Zm5.625 0c0 .414-.168.75-.375.75s-.375-.336-.375-.75.168-.75.375-.75.375.336.375.75Zm-.375 0h.008v.015h-.008V9.75Z" />
            </svg>
            <p className="text-lg font-bold">
                The requested company not found!
            </p>
            <Link
                href="/companies"
                className="btn btn-secondary btn-md rounded px-4 py-2 text-sm">
                Go Back
            </Link>
        </div>
    </main>
  )
}

export default CompanyNotFound