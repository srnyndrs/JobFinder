import React from 'react'
import BannerPicture from '../../../public/banner_job.jpg'

const JobBanner = () => {
    return (
        <div className="bg-cover bg-center h-64" style={{background: `url(${BannerPicture.src})`}}>
            <div className={`flex h-full w-full bg-black bg-opacity-35`}>
            </div>
        </div>
    )
}

export default JobBanner