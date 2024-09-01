import React from 'react';
import BannerPicture from '../../public/banner.jpg';

const Banner = () => {
    return (
      <div className="w-full h-28 md:h-52 rounded-b-3xl bg-cover bg-center shadow-xl" 
           style={{ background: `url(${BannerPicture.src})`,}}
      />
    );
}

export default Banner;