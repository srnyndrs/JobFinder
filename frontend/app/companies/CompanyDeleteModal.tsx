'use client'
import React, { useState } from 'react'
import { Company } from '../domain/Company';
import { useRouter } from 'next/navigation';

interface Props {
    company: Company
}

const CompanyDeleteModal = ({company}: Props) => {
    const router = useRouter();

    const [error, setError] = useState<string | undefined>();
    const [loading, setLoading] = useState(false);

    const onCompanyDelete = async (event: React.MouseEvent) => {
      event.preventDefault();

      setError(undefined);
      setLoading(true);

      await fetch(`http://localhost:8080/v1/companies/${company.id}`,
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
                router.push(`/companies`);
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
              <div className="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
                  <div className="relative transform overflow-hidden rounded-lg w-11/12 max-w-5xl">
                      <div className="bg-white px-4 pb-4 pt-5 sm:p-6 sm:pb-4">
                          <div className="flex justify-between items-center">
                              <p className="text-lg font-bold">
                                  Delete Company
                              </p>
                          </div>
                          <div className="divider"/>
                          <div className="space-y-4 md:space-y-8">
                            { loading &&
                                <span className="loading loading-spinner loading-md"></span>
                                || error && 
                                <div className='w-full font-semibold text-error-content bg-error p-2 bg-opacity-30 rounded-md justify-center'>
                                    {error}
                                </div>
                            }
                              <p className="text-xl">
                                  Are you sure you want to delete <strong>{company.name}</strong> company?
                              </p>
                              <div className="join join-horizontal space-x-8">
                                  <button onClick={onCompanyDelete} type="button" className="justify-center rounded-md bg-red-800 px-3 py-2 text-sm font-semibold text-white shadow-sm ring-1 ring-inset ring-black hover:bg-gray-6000 sm:mt-0 sm:w-auto">Delete</button>
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

export default CompanyDeleteModal;