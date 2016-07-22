import OCEFilters from "../../oce/filters";
import OrganizationsTab from "../../oce/filters/tabs/organizations";
import ProcurementTypes from "./tabs/procurement-types";
import Dates from "./tabs/dates";
import Locations from "./tabs/locations";

class OCVNFilters extends OCEFilters{
}

OCVNFilters.TABS=[OrganizationsTab, ProcurementTypes, Dates, Locations];

export default OCVNFilters;