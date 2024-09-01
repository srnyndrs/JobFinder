import { Job } from "./domain/Job";
import FilterSection from "./components/FilterSection";
import { filterData, SearchType } from 'filter-data';
import JobsTable from "./components/JobsTable";
import Banner from "./components/Banner";

interface Props {
  searchParams: {
      jobType?: string
      remote?: string
      location?: string
      query?: string
  }
}

interface FilterCondition {
  key: string | string[] | string[][];
  value?: string | number | boolean | Date;
  type: SearchType;
}

export default async function Home({searchParams:{ jobType, remote, location, query }}: Props) {
    const jobs: Job[] = await fetch("http://localhost:8080/v1/jobs", { cache: "no-store" })
      .then(response => {
          if(!response.ok) {
              return [];
          }
          return response.json();
      })
      .catch(() => { throw new Error("Failed to fetch companies")} );

    const locations = jobs.map(job => job.location).filter((value, index, self) => self.indexOf(value) === index);

    const orFilterData = (data: Job[], conditions: FilterCondition[] | undefined): Array<Job> => {
      if(conditions === undefined) return data;
      const results = conditions!.map(condition => filterData(data, [condition]));
      return Array.from(new Set(results.flat()));
    };
    
    const remoteFilterConditions = (remote !== undefined) ? remote!.split(',').map( filter => (
      {
          key: 'remote',
          value: filter,
          type: SearchType.LK,
      }
    )) : undefined

    const jobTypeFilterConditions = (jobType !== undefined) ? jobType!.split(',').map( filter => (
      {
          key: 'jobType',
          value: filter,
          type: SearchType.LK,
      }
    )) : undefined

    const locationFilterConditions = (location !== undefined) ? location!.split(',').map( filter => (
      {
          key: 'location',
          value: filter,
          type: SearchType.LK,
      }
    )) : undefined

    const filteredRemote = orFilterData(jobs, remoteFilterConditions);
    const filteredLocation = orFilterData(jobs, locationFilterConditions)
    const filteredJobType = orFilterData(jobs, jobTypeFilterConditions)

    const filteredJobs = new Set(filteredRemote
      .filter(function(job) {
          return filteredLocation.indexOf(job) !== -1;
      })
      .filter(function(job) {
          return filteredJobType.indexOf(job) !== -1;
      })
      .filter(function(job) {
        const value = `${JSON.stringify(job)}`.toLowerCase();
        let filter = (query ?? "").toLowerCase();
        return value.indexOf(filter) > -1;
      })
    );

    return (
      <div className="w-full">
        <Banner/>
        <div className="grid grid-cols-1 justify-center lg:justify-start lg:grid-cols-5 gap-2 my-1 lg:my-2 mx-1 lg:mx-3">
          <div className="col-span-4 lg:col-span-1 rounded-md h-fit w-full">
            <FilterSection locations={locations}/>
          </div>
          <div className="col-span-4 lg:px-4 h-fit">
            <JobsTable jobs={Array.from(filteredJobs)}/>
            { filteredJobs.size == 0 && 
              <div className="px-4 text-lg ">
                No results by the given parameters.
              </div>
            }
          </div>
        </div>
      </div>
    );
}
