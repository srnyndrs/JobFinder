'use client'
import Image from 'next/legacy/image'
import defaultLogo from '../logo.svg'
import { useState } from 'react'

interface Props {
    name: string
    image: string
    width?: number
    height?: number
}

export function CompanyLogo({name, image, width, height}: Props) {
    const [loading, setLoading] = useState(true);
    const [source, setSource] = useState(`/company_images/${image}`);
    const [error, setError] = useState(false);
    
    const imageWidth: number = width ?? 64;
    const imageHeight: number = height ?? 64;

    return (
        <div className="avatar">
            <div className={`rounded-md shadow-xl ${((error || loading) ? "bg-gray-200" : "")}`}>
                { !error ?
                    <Image 
                        src={source}
                        alt={`${name}s logo`}
                        width={imageWidth}
                        height={imageHeight}
                        quality={100}
                        objectPosition="center"
                        onLoad={() => setLoading(false)}
                        onError={() => {
                            setSource(defaultLogo); // Fallback to default logo if image fails to load
                            setError(true);
                        }}
                    />
                    :
                    <div style={{width: imageWidth, height: imageHeight}}>
                        <div className="flex h-full w-full justify-center items-center">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="#000" className="size-12">
                                <path strokeLinecap="round" strokeLinejoin="round" d="m2.25 15.75 5.159-5.159a2.25 2.25 0 0 1 3.182 0l5.159 5.159m-1.5-1.5 1.409-1.409a2.25 2.25 0 0 1 3.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 0 0 1.5-1.5V6a1.5 1.5 0 0 0-1.5-1.5H3.75A1.5 1.5 0 0 0 2.25 6v12a1.5 1.5 0 0 0 1.5 1.5Zm10.5-11.25h.008v.008h-.008V8.25Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Z" />
                            </svg>
                        </div>
                    </div>
                }
            </div>
        </div>
    );
}