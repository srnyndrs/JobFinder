import React from 'react'
import { Company } from '../domain/Company';
import Link from 'next/link';
import CompanyCreatorModal from './CompanyCreatorModal';
import CompanyDeleteModal from './CompanyDeleteModal';

interface Props {
    searchParams: {
        create?: boolean
        edit?: number
        deleteCompany?: number
    }
}

const CompaniesPage = async ({searchParams: {create, edit, deleteCompany}}: Props) => {
    const companies: Company[] | undefined = await fetch("http://localhost:8080/v1/companies", { cache: 'no-store' })
        .then(async (response) => {
            if (response.ok) return response.json();
        })
        .catch(() => { throw new Error("Failed to fetch companies")} );

    const selectedCompanyForEdit = companies?.find((c) => c.id == edit);
    const selectedCompanyForDelete = companies?.find((c) => c.id == deleteCompany);

    return (
        <>
            <div className="mx-6 mb-6">
                <div className="flex flex-row items-center justify-between pt-4">
                    <h1>Companies</h1>
                    <Link className="btn btn-primary btn-outline btn-sm" href={"/companies?create=true"}>
                        <svg className="size-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
                        </svg>
                        <p>New company</p>
                    </Link>
                </div>             
                <div className="overflow-x-auto">
                    <table className='table table-auto'>
                        <thead className="text-md">
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Image</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            { companies && companies.map(company =>
                            <tr className="tableBody" key={company.id}>
                                <td>{company.id}</td>
                                <td>
                                    <Link className="link" href={`/companies/${company.id}`}>{company.name}</Link>
                                </td>
                                <td>{company.image}</td>
                                <td className="join join-horizontal space-x-4">
                                    <Link href={`/companies?edit=${company.id}`}>
                                        <p className="btn btn-xs btn-primary btn-outline w-max h-fit">Edit</p>
                                    </Link>
                                    <Link href={`/companies?deleteCompany=${company.id}`}>
                                        <p className="btn btn-xs btn-error btn-outline w-max h-fit">Delete</p>
                                    </Link>
                                </td>
                            </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
            { companies && (create && <CompanyCreatorModal /> 
                || edit && selectedCompanyForEdit && <CompanyCreatorModal originalCompany={selectedCompanyForEdit}/>
                || deleteCompany && selectedCompanyForDelete && <CompanyDeleteModal company={selectedCompanyForDelete}/>)
            }
        </>
    );
}

export default CompaniesPage;