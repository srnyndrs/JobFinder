'use client'
import { useRouter } from 'next/navigation';
import React, { useState } from 'react'
import { Company, companyDescriptionTemplate } from '../domain/Company';
import DescriptionEditor from '../components/DescriptionEditor';

interface Props {
    originalCompany?: Company
}

const CompanyCreatorForm = ({originalCompany}: Props) => {
    const router = useRouter();
    const [loading, setLoading] = useState(false);

    const requestPath = originalCompany === undefined ? "http://localhost:8080/v1/companies" : `http://localhost:8080/v1/companies/${originalCompany.id}`;
    const method = originalCompany === undefined ? "POST" : "PUT";

    const [company, setCompany] = useState(originalCompany !== undefined
        ? originalCompany!
        : {
            name: "" ,
            description: "",
            image: ""
        }
    );

    const [error, setError] = useState<string[] | undefined>();

    const formValidation = () => {
        return [
            ...(company.name.length === 0 ? ["Name is required!"]: []),
            ...(company.description.length === 0 ? ["Description is required!"] : [])
        ]
    };

    const handleFieldChange = (event: React.ChangeEvent<any>) => {
        const value = event.target.value;
        setCompany({ ...company, [event.target.name]: value });
    };

    const handleDescriptionChange = (value: string) => {
        setCompany({ ...company, "description": value });
    };

    const saveCompany = async (event: React.MouseEvent) => {
        event.preventDefault();

        setError([]);
        setLoading(true);

        const validationResult = formValidation();

        if(validationResult.length === 0) {
            await fetch(
                requestPath,
                {
                    method: method,
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json',
                    },
                    body: JSON.stringify(company)
                }
            )
            .then((response) => {
                if(response.ok) {
                    router.push('/companies');
                    router.refresh();
                }
            })
            .catch((error) => { setError([`${error.message}`]) })
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
              || 

              error &&
                <div className="w-full justify-center mb-4 space-y-2">
                    { error.map( (message, index) =>
                        <p key={index} className="font-semibold text-error-content bg-error p-2 bg-opacity-30 rounded-md">{message}</p>
                    )}
                </div>
            }
            <form className="w-full space-y-6 h-2/4">
                <div className="flex-auto w-full join sm:join-vertical md:join-horizontal space-x-6 items-center">
                    <div className="w-1/2">
                        <div className="text-left">
                            <label className="text-gray-500 font-bold" htmlFor="inline-name">
                                Name<span className="text-red-700">*</span>
                            </label>
                        </div>
                        <div className="w-full">
                            <input name="name" onChange={(e) => handleFieldChange(e)} id="inline-name" value={company.name} type="text" placeholder="Infinite Ideas"
                                className="bg-gray-200 appearance-none border-2 border-gray-200 rounded w-full py-2 px-4"
                            />
                        </div>
                    </div>
                    <div className="w-1/2">
                        <div className="text-left">
                            <label className="text-gray-500 font-bold" htmlFor="inline-image">
                                Image
                            </label>
                        </div>
                        <div className="w-full">
                            <input name="image" onChange={(e) => handleFieldChange(e)} id="inline-image" value={company.image} type="text" placeholder="company-logo.jpg"
                                className="bg-gray-200 appearance-none border-2 border-gray-200 rounded w-full py-2 px-4"
                            />
                        </div>
                    </div>
                </div>
                <div className="flex-auto w-full items-top">
                    <div className="text-left">
                        <label className="text-gray-500 font-bold" htmlFor="inline-description">
                            Description<span className="text-red-700">*</span>
                        </label>
                    </div>
                    <div className="w-full">
                        <DescriptionEditor defaultValue={company.description} template={companyDescriptionTemplate} onChange={handleDescriptionChange} />
                    </div>
                </div>
                <div className="items-center">
                    <button type="button" className="btn btn-outline btn-primary py-2 px-4" onClick={(e)=>{saveCompany(e)}}>
                        Save Company
                    </button>
                </div>
            </form> 
        </>
    );
}

export default CompanyCreatorForm;