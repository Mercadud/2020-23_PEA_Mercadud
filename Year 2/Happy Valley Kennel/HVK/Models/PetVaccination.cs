using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

#nullable disable

namespace HVK.Models
{
    public partial class PetVaccination
    {
        [DataType(DataType.Date)]
        [DisplayFormat(DataFormatString = "{0:yyyy\\/MM\\/dd}")]
        public DateTime ExpiryDate { get; set; }
        public int VaccinationId { get; set; }
        public int PetId { get; set; }
        [Display(Name = "Checked")]
        public bool VaccinationChecked { get; set; }

        public virtual Pet Pet { get; set; }
        public virtual Vaccination Vaccination { get; set; }
    }
}
