'use client'
import { useRouter } from 'next/navigation';
import React, { useState } from 'react'
import { Job } from '../domain/Job';

interface Props {
    job: Job
}

const JobDeleteModal = ({job}: Props) => {
    const router = useRouter();

    const [error, setError] = useState<string | undefined>();
    const [loading, setLoading] = useState(false);

    const onJobDelete = async (event: React.MouseEvent) => {
        event.preventDefault();

        setError(undefined);
        setLoading(true);

        await fetch(`http://localhost:8080/v1/jobs/${job.id}`,
            {
                method: "DELETE",
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                }
            }
        )
            .then((response) => {
                if(response.ok) {
                    router.push("/jobs");
                    router.refresh();
                }
            })
            .catch((error) => { setError(`${error.message}`) })
            .finally(() => setLoading(false));
    }

    return (
        <div className="relative z-10" aria-labelledby="modal-title" role="dialog" aria-modal="true">
            <div className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true" />
            <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
                <div className="flex min-h-full justify-center text-center items-center p-1 md:p-4">
                    <div className="relative transform overflow-hidden rounded-lg w-full me-5 md:w-11/12 md:max-w-5xl">
                        <div className="bg-white px-4 pb-4 pt-5 space-y-0 md:space-y-2">
                            <div className="flex justify-between items-center">
                                <p className="text-lg font-bold">
                                    Delete Job
                                </p>
                            </div>
                            <div className="divider"/>
                            <div className="space-y-4 md:space-y-8">
                                { loading &&
                                    <span className="loading loading-spinner loading-md"></span>
                                    || error && 
                                    <div className="w-full font-semibold text-error-content bg-error p-2 bg-opacity-30 rounded-md justify-center">
                                        {error}
                                    </div>
                                }
                                <p className="text-lg md:text-xl">
                                    Are you sure you want to delete <strong>{job.company.name}</strong>&aposs <strong>{job.title}</strong> job?
                                </p>
                                <div className="join join-horizontal space-x-8">
                                    <button onClick={onJobDelete} type="button" className="justify-center rounded-md bg-red-800 px-3 py-2 text-sm font-semibold text-white shadow-sm ring-1 ring-inset ring-black hover:bg-gray-6000 sm:mt-0 sm:w-auto">Delete</button>
                                    <button onClick={router.back} type="button" className="justify-center rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 sm:mt-0 sm:w-auto">Cancel</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default JobDeleteModal;