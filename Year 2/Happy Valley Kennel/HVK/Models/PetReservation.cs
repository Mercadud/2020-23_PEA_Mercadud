using System;
using System.Collections.Generic;

#nullable disable

namespace HVK.Models
{
    public partial class PetReservation
    {
        public PetReservation()
        {
            Medications = new HashSet<Medication>();
            PetReservationDiscounts = new HashSet<PetReservationDiscount>();
            PetReservationServices = new HashSet<PetReservationService>();
        }

        public int PetReservationId { get; set; }
        public int PetId { get; set; }
        public int ReservationId { get; set; }
        public int? RunId { get; set; }

        public virtual Pet Pet { get; set; }
        public virtual Reservation Reservation { get; set; }
        public virtual Run Run { get; set; }
        public virtual ICollection<Medication> Medications { get; set; }
        public virtual Medication Medication2 { get; set; }
        public virtual ICollection<PetReservationDiscount> PetReservationDiscounts { get; set; }
        public virtual ICollection<PetReservationService> PetReservationServices { get; set; }
    }
}
