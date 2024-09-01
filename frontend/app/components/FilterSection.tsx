'use client'
import React, { useState } from 'react'
import { JobType } from '../domain/JobType'
import { RemoteType } from '../domain/RemoteType';
import { useRouter, useSearchParams } from 'next/navigation';
import { useCallback } from "react";

interface Props {
    locations: string[]
}

interface Filter {
    name: string,
    type: string,
    value: boolean
}

const FilterSection = ({locations}: Props) => {
    const jobTypes = Object.values(JobType);
    const remoteTypes = Object.values(RemoteType);

    const router = useRouter();
    const searchParams = useSearchParams()!;

    const createQueryString = useCallback(
        (name: string, value: string[]) => {
          const params = new URLSearchParams(searchParams);
          params.set(name, `${value}`);
     
          return `${name}=${value}`;
        },
        [searchParams]
      );

    const onChange = () => {
        const filteredItems = filters
                .filter(filter => filter.value)
                .reduce((acc, filter) => {
                    if (!acc[filter.type]) {
                        acc[filter.type] = [];
                    }
                    acc[filter.type].push(filter.name);
                    return acc;
                }, {} as { [key: string]: string[] });

        const array = Object.entries(filteredItems).map(([key, value]) => createQueryString(key, value));

        if(query.length !== 0) {
            array.push(`query=${query}`);
        }

        router.push(`http://localhost:3000?${array.join('&')}`);
    }

    const [query, setQuery] = useState("");

    const [filters, setFilters] = useState<Filter[]>(
        [
            ...Object.values(JobType).map((a) => ({ name: a, type: "jobType", value: true })),
            ...Object.values(RemoteType).map((a) => ({ name: a, type: "remote", value: true })),
            ...locations.map((a) => ({ name: a, type: "location", value: true }))
        ]
    );

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const name = event.target.name;
        const value = event.target.checked;

        setFilters(prevState => {
            return prevState.map(item => {
                  if (item.name === name) {
                    return { ...item, value: value };
                  }
                  return item;
            })
        });
    }

    const handleKeyDown = (event: React.KeyboardEvent) => {
        if (event.key === 'Enter') {
           onChange();
        }
     }

    return (
        <div className="flex-auto space-y-4 p-3 w-full">
            <div className="flex px-4 py-3 rounded-md border-2 border-primary overflow-hidden w-full mx-auto">
                <input type="text" placeholder="Search" className="w-full outline-none text-md bg-transparent" onKeyDown={handleKeyDown} onChange={(e) => {setQuery(e.target.value)}}/>
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 192.904 192.904" width="16px" className="">
                    <path d="m190.707 180.101-47.078-47.077c11.702-14.072 18.752-32.142 18.752-51.831C162.381 36.423 125.959 0 81.191 0 36.422 0 0 36.423 0 81.193c0 44.767 36.422 81.187 81.191 81.187 19.688 0 37.759-7.049 51.831-18.751l47.079 47.078a7.474 7.474 0 0 0 5.303 2.197 7.498 7.498 0 0 0 5.303-12.803zM15 81.193C15 44.694 44.693 15 81.191 15c36.497 0 66.189 29.694 66.189 66.193 0 36.496-29.692 66.187-66.189 66.187C44.693 147.38 15 117.689 15 81.193z"/>
                </svg>
            </div>
            <div className="p-3 space-y-2 rounded-md border-2 border-primary">
                <div className="flex items-center justify-center">
                    <p className='text-2xl font-bold m-2'>Filters</p>
                </div>
                <details className="collapse collapse-arrow">
                    <summary className="collapse-title text-md font-bold">
                        <p>Job Type</p>
                    </summary>
                    <div className="collapse-content space-y-2">
                    { jobTypes.map( (jobType, index) =>
                        <div key={index} className="form-control">
                            <label className="label cursor-pointer">
                                <span className='label-text'>{jobType}</span>
                                <input tabIndex={index+1} onChange={(e) => {handleChange(e)}} className="toggle" id={`jobType-${index}`} type="checkbox" name={jobType} defaultChecked/>
                            </label>
                        </div>
                    )}
                    </div>
                </details>
                <details className="collapse collapse-arrow">
                    <summary className="collapse-title text-md font-bold">
                        <p>Remote</p>
                    </summary>
                    <div className="collapse-content space-y-2">
                    { remoteTypes.map( (remoteType, index) =>
                        <div key={index}  className="form-control">
                            <label className="label cursor-pointer">
                                <span className="label-text">{remoteType}</span>
                                <input tabIndex={index+1} onChange={(e) => {handleChange(e)}} className="checkbox" id={`remoteType-${index}`} type="checkbox" name={remoteType} value={remoteType} defaultChecked />
                            </label>
                        </div>
                    )}
                    </div>
                </details>
                <details className="collapse collapse-arrow">
                    <summary className="collapse-title text-md font-bold">
                        <p>Location</p>
                    </summary>
                    <div className="collapse-content space-y-2">
                    { locations.map( (location, index) =>
                        <div key={index} className="form-control">
                            <label className="label cursor-pointer">
                                <span className='label-text'>{location}</span>
                                <input tabIndex={index+1} onChange={(e) => {handleChange(e)}} className="toggle" id={`location-${index}`} type="checkbox" name={location} defaultChecked/>
                            </label>
                        </div>
                    )}
                    </div>
                </details>
                <div className="flex items-center justify-center">
                    <button className='btn btn-secondary rounded-md text-base-200 w-1/4 lg:w-full' onClick={() => {onChange()}}>Apply</button>
                </div>
            </div>
        </div>
    );
}

export default FilterSection;