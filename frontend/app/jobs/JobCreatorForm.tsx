'use client'
import React, { useState } from 'react'
import { Company } from '../domain/Company';
import { useRouter } from 'next/navigation';
import { Job, JOB_DESCRIPTION_TEMPLATE } from '../domain/Job';
import { JobType } from '../domain/JobType';
import { RemoteType } from '../domain/RemoteType';
import DescriptionEditor from '../components/DescriptionEditor';
import moment from 'moment';

interface Props {
    companies: Company[]
    originalJob?: Job
}

const JobCreatorForm = ({companies, originalJob}: Props) => {
    const router = useRouter();

    const requestPath = (originalJob === undefined) ? "http://localhost:8080/v1/jobs" : `http://localhost:8080/v1/jobs/${originalJob.id}`;
    const method = (originalJob === undefined) ? "POST" : "PUT";

    const [job, setJob] = useState(originalJob !== undefined 
        ? originalJob!
        : {
            title: "",
            description: "",
            location: "",
            created: "",
            jobType: "Full-Time",
            remote: "Remote",
            company: {
                id: companies[0].id
            }
        }
    );

    const [error, setError] = useState<string[] | undefined>();
    const [loading, setLoading] = useState(false);

    const formValidation = () => {
        return [
            ...(job.title.length === 0 ? ["Title is required!"] : []),
            ...(job.description.length === 0 ? ["Description is required!"] : []),
            ...(job.location.length === 0 ? ["Location is required!"] : [])
        ]
    };

    const jobTypes = Object.values(JobType);

    const remoteTypes = Object.values(RemoteType);

    const handleFieldChange = (event: React.ChangeEvent<any>) => {
        const value = event.target.value;
        setJob({...job, [event.target.name]: value});
    };

    const handleDescriptionChange = (value: string) => {
        setJob({...job, "description": value});
    };

    const handleCompanyChange = (event: React.ChangeEvent<any>) => {
        const value = event.target.value;
        setJob({...job, company: { id: value }});
    };

    const saveJob = async (event: React.MouseEvent) => {
        event.preventDefault();

        setError([]);
        setLoading(true);

        const validationResult = formValidation();

        if(validationResult.length === 0) {
            // Update the time
            job.created = moment().format('YYYY-MM-DD HH:mm');
            // Send request
            await fetch(
                requestPath,
                {
                    method: method,
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json',
                    },
                    body: JSON.stringify(job)
                }
            )
            .then((response) => {
                if (response.ok) {
                    router.push('/jobs');
                    router.refresh();
                }
            })
            .catch((error) => setError([`${error}`]))
            .finally(() => setLoading(false));
        } else {
            setError(validationResult);
            setLoading(false);
        }
    };

    return (
        <>
            { loading &&
                    <div className="w-full justify-center">
                        <span className="loading loading-spinner loading-md"></span>
                    </div>
                || error &&
                    <div className="w-full justify-center mb-4 space-y-2">
                        { error.map( (message, index) =>
                            <p key={index} className="font-semibold p-2 text-error-content bg-error bg-opacity-30 rounded-md">{message}</p>
                        )}
                    </div>
            }
            <form className="w-full space-y-6">
                <div className="flex-auto w-full join join-vertical md:join-horizontal space-y-6 md:space-y-0 md:space-x-6 md:items-center">
                    <div className="md:w-1/2">
                        <div className="text-left">
                            <label className="text-gray-500 font-bold" htmlFor="inline-title">
                                Job Title<span className="text-red-700">*</span>
                            </label>
                        </div>
                        <div className="w-full">
                            <input name="title" onChange={(e) => handleFieldChange(e)} id="inline-title" value={job?.title} type="text" className="bg-gray-200 appearance-none border-2 border-gray-200 rounded w-full py-2 px-4"/>
                        </div>
                    </div>
                    <div className="md:w-1/2">
                        <div className="text-left">
                            <label className="text-gray-500 font-bold" htmlFor="inline-location">
                                Location<span className="text-red-700">*</span>
                            </label>
                        </div>
                        <div className="w-full">
                            <input name="location" onChange={(e) => handleFieldChange(e)} id="inline-location" value={job?.location} type="text" className="bg-gray-200 appearance-none border-2 border-gray-200 rounded w-full py-2 px-4"/>
                        </div>
                    </div>
                </div>
                <div className="mb-6">
                    <div className="text-left">
                        <label className="text-gray-500 font-bold" htmlFor="inline-description">
                            Description<span className="text-red-700">*</span>
                        </label>
                    </div>
                    <div className="w-full">
                        <DescriptionEditor defaultValue={job.description} template={JOB_DESCRIPTION_TEMPLATE} onChange={handleDescriptionChange}/>
                    </div>
                </div>
                <div className="flex-auto w-full join join-vertical md:join-horizontal space-y-4 md:space-y-0 justify-between md:items-center">
                    <div className="md:w-1/4">
                        <div className="text-left">
                            <label className="text-gray-500 font-bold" htmlFor="job-type">
                                Job Type<span className="text-red-700">*</span>
                            </label>
                        </div>
                        <div className="w-full">
                            <select id="job-type" value={job.jobType} onChange={(e) => handleFieldChange(e)} name="jobType" className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500">
                                { jobTypes.map((type, index) =>
                                    <option key={index} value={type}>{type}</option>
                                )}
                            </select>
                        </div>
                    </div>
                    <div className="md:w-1/4">
                        <div className="text-left">
                            <label className="text-gray-500 font-bold" htmlFor="remote">
                                Remote<span className="text-red-700">*</span>
                            </label>
                        </div>
                        <div className="w-full">
                            <select id="remote" value={job.remote} onChange={(e) => handleFieldChange(e)} name="remote" className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500">
                                { remoteTypes.map((type,index) =>
                                    <option key={index} value={type}>{type}</option>
                                )}
                            </select>
                        </div>
                    </div>
                    <div className="md:w-1/4">
                        <div className="text-left">
                            <label className="text-gray-500 font-bold" htmlFor="company-id">
                                Company<span className="text-red-700">*</span>
                            </label>
                        </div>
                        <div className="w-full">
                            <select id="company-id" value={job.company.id} onChange={(e) => handleCompanyChange(e)} name="company-id" className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500">
                                { companies.map( (company) =>
                                    <option key={company.id} value={company.id}>{company.name}</option>
                                )}
                            </select>
                        </div>
                    </div>
                </div>
                <div className=" items-center">
                    <button type="button" className="btn btn-outline rounded-md btn-primary py-2 px-4" onClick={(e)=>{saveJob(e)}}>
                        Save Job
                    </button>
                </div>
            </form>
        </>
    );
}

export default JobCreatorForm;