'use client'
import { useRouter } from "next/navigation";
import CompanyCreatorForm from "./CompanyCreatorForm";
import { Company } from "../domain/Company";

interface Props {
    originalCompany?: Company
}

const CompanyCreatorModal = ({originalCompany}: Props) => {
    const router = useRouter();

    return (
      <div className="relative z-10" aria-labelledby="modal-title" role="dialog" aria-modal="true">
          <div className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true"></div>
          <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
              <div className="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
              <div className="relative transform overflow-hidden rounded-lg w-11/12 max-w-5xl">
                  <div className="bg-white px-4 pb-4 pt-5 sm:p-6 sm:pb-4">
                      <div className="flex justify-between items-center">
                          <p className="text-lg font-bold">
                            { originalCompany === undefined ? "Create Company" : "Edit Company" }
                          </p>
                          <button onClick={router.back} type="button" className="justify-center rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 sm:mt-0 sm:w-auto">Cancel</button>
                      </div>
                      <div className="divider"/>
                      <CompanyCreatorForm originalCompany={originalCompany}/>
                      </div>
                  </div>
              </div>
          </div>
      </div>
    );
}

export default CompanyCreatorModal;