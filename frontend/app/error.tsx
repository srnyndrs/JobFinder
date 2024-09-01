'use client'
import React from 'react'

interface Props {
    error: Error & { digest?: string }
    reset: () => void
}

const GlobalError = ({error, reset}: Props) => {
    return (
        <main className="flex h-full flex-col items-center justify-center py-6 space-y-4">
            <h2 className="text-lg text-center font-semibold">Something went wrong!</h2>
            <button className="btn btn-primary" onClick={() => reset()}>
                Try again
            </button>
        </main>
    );
}

export default GlobalError;