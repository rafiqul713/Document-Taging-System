using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Configuration;
using System.Web.Http;
using System.Web.Http.Description;
using DTS.Models;
using DTS.Models.DTO;
using Microsoft.AspNet.Identity;

namespace DTS.Controllers
{
    [Authorize]
    [RoutePrefix("api/User")]
    public class UserController : ApiController
    {

        ApplicationDbContext db = new ApplicationDbContext();
        // GET api/User/info
        [Route("info")]
        public async Task<IHttpActionResult> GetUserInfo()
        {
            string id = User.Identity.GetUserId();       // get the userID

            var user = await (from u in db.Users
                              join up in db.UserProfiles on u.Id equals up.UserId
                              join uacl in db.UserAcls on u.Id equals uacl.UserId
                              join c in db.Companies on u.CompanyId equals c.Id
                              where u.Id == id
                              select new
                              {
                                  u.Id,
                                  u.UserName,
                                  u.Email,
                                  u.PasswordHash,
                                  up.FirstName,
                                  up.LastName,
                                  CompanyName = c.Name,
                                  UserType = uacl.UserType == 0 ? "Normal" : "Admin",


                              }).ToListAsync();

            if (user == null)
            {
                return NotFound();
            }


            return Ok(user);
        }


        // PUT api/User/5
        [Route("{id}")]
        public async Task<IHttpActionResult> PutUser(string id, UserDTO user)
        {
            var UserId = User.Identity.GetUserId();
            // is id is valid
            if (!(db.Users.Any(x => x.Id == UserId)))
            {
                string message = "Invalid Id";
                return BadRequest(message);
            }

            //Change in User Model


            var aUser = await db.Users.Where(x => x.Id == UserId).SingleOrDefaultAsync();
            //aUser.UserName = user.UserName;  // as only userName can be changed

            //Change in UserProfile Model
            UserProfile aUserProfile = new UserProfile();
            aUserProfile = await db.UserProfiles.Where(x => x.UserId == id).SingleOrDefaultAsync();
            aUserProfile.FirstName = user.FirstName;
            aUserProfile.LastName = user.LastName;



            db.Entry(aUser).State = EntityState.Modified;
            db.Entry(aUserProfile).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!UserExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            //UserDTO with updated Data
            UserDTO aUserDto = new UserDTO();
            aUserDto.Id = aUser.Id;
            aUserDto.UserName = aUser.UserName;
            aUserDto.Email = aUser.Email;
            aUserDto.Password = aUser.PasswordHash;
            aUserDto.CompanyId = aUser.CompanyId;
            aUserDto.FirstName = aUserProfile.FirstName;
            aUserDto.LastName = aUserProfile.LastName;


            return Ok(aUserDto);
        }

        // DELETE api/User/5
        [Route("{id}")]

        public async Task<IHttpActionResult> DeleteUser(string id)
        {
            var user = await db.Users.Where(x => x.Id == id).SingleOrDefaultAsync();
            var aUserProfile = await db.UserProfiles.Where(x => x.UserId == id).SingleOrDefaultAsync();
            var aUserAcl = await db.UserAcls.Where(x => x.UserId == id).SingleOrDefaultAsync();
            if (user == null)
            {
                return NotFound();
            }

            db.Users.Remove(user);
            db.UserAcls.Remove(aUserAcl);
            db.UserProfiles.Remove(aUserProfile);
            await db.SaveChangesAsync();

            return Ok("User is deleted");
        }



        [Route("{id}/Acl")]
        public async Task<IHttpActionResult> GetACl(string id)
        {
            // Check if User exist
            if (!UserExists(id))
            {
                BadRequest("User id is not valid");
            }

            // If User want info from different id
            if (User.Identity.GetUserId() == id)
            {
                BadRequest("This id is not User's Id");
            }

            UserACL aUserAcl = await db.UserAcls.Where(x => x.UserId == id).SingleOrDefaultAsync();
            return Ok(aUserAcl);

        }

        [Route("{id}/Documents")]
        public async Task<IHttpActionResult> GetDocuments(string id)
        {
            if (!UserExists(id))
            {
                BadRequest("User id is not valid");
            }

            // If User want info from different id
            var UserId = User.Identity.GetUserId();
            if (UserId != id)
            {
                BadRequest("This id is not User's Id");
            }


            var documentList = await (from u in db.Users
                                      join d in db.Documents on u.Id equals d.UserId into du
                                      from document in du.DefaultIfEmpty()

                                      join t in db.Tags on document.Id equals t.DocumentId into td
                                      from tag in td.DefaultIfEmpty()

                                      join h in db.Histories on document.Id equals h.DocumentId into hd
                                      from history in hd.DefaultIfEmpty()

                                      join hs in db.HistorySequences on history.Id equals hs.HistoryId into hsh
                                      from historySequence in hsh.DefaultIfEmpty()

                                      join f in db.Files on document.Id equals f.DocumentId into fd
                                      from file in fd.DefaultIfEmpty()
                                      where u.Id == id
                                      select new
                                      {
                                          Document = document,
                                          File = file,
                                          Tags = tag,
                                          History = history,
                                          HistorySequences = historySequence,

                                      }).ToListAsync();

            return Ok(documentList);

        }



        //private static UserDTO GetUserInfo(User aUser, UserProfile aUserProfile)
        //{
        //    UserDTO user = new UserDTO();
        //    user.Id = aUser.Id;
        //    user.UserName = aUser.UserName;
        //    user.Email = aUser.Email;
        //    user.Password = aUser.Password;
        //    user.CompanyId = aUser.CompanyId;
        //    user.FirstName = aUserProfile.FirstName;
        //    user.LastName = aUserProfile.LastName;
        //    return user;
        //}



        private bool UserExists(string id)
        {
            return db.Users.Count(e => e.Id == id) > 0;
        }


    }
}
